package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport_;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase_;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisStages;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisStatuses;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis_;
import ar.edu.utn.frba.proyecto.sigo.exception.BusinessConstrainException;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import com.google.common.collect.Lists;
import spark.QueryParamsMap;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class AnalysisService extends SigoService<Analysis, Analysis>{

    private AnalysisCaseService caseService;

    @Inject
    public AnalysisService(HibernateUtil hibernateUtil, AnalysisCaseService caseService) {
        super(Analysis.class, hibernateUtil.getSessionFactory());

        this.caseService = caseService;
    }

    public List<Analysis> find(QueryParamsMap parameters){

        CriteriaBuilder builder = currentSession().getCriteriaBuilder();

        CriteriaQuery<Analysis> criteria = builder.createQuery(Analysis.class);

        Root<Analysis> analysis = criteria.from(Analysis.class);

        Join<Object, Object> analysisCase = analysis.join(Analysis_.analysisCase.getName());

        Join<Object, Object> airport = analysisCase.join(AnalysisCase_.aerodrome.getName());


        Optional<Predicate> predicateId = Optional
                .ofNullable(parameters.get(Airport_.id.getName()).value())
                .map(v -> builder.equal(airport.get(Airport_.id.getName()), v));

        Optional<Predicate> predicateNameFIR = Optional
                .ofNullable(parameters.get(Airport_.nameFIR.getName()).value())
                .map(v -> builder.like(airport.get(Airport_.nameFIR.getName()), String.format("%%%s%%",v)));

        Optional<Predicate> predicateCodeFIR = Optional
                .ofNullable(parameters.get(Airport_.codeFIR.getName()).value())
                .map(v -> builder.equal(airport.get(Airport_.codeFIR.getName()), v));

        Optional<Predicate> predicateCodeIATA = Optional
                .ofNullable(parameters.get(Airport_.codeIATA.getName()).value())
                .map(v -> builder.equal(airport.get(Airport_.codeIATA.getName()),v));

        Optional<Predicate> predicateCodeLocal = Optional
                .ofNullable(parameters.get(Airport_.codeLocal.getName()).value())
                .map(v -> builder.equal(airport.get(Airport_.codeLocal.getName()),v));

        Optional<Predicate> predicateCurrent = Optional
                .ofNullable(parameters.get("current"))
                .map(p -> {

                    Subquery<Analysis> subquery = criteria.subquery(Analysis.class);

                    Root<Analysis> otherAnalysis = subquery.from(Analysis.class);

                    Predicate predicateParent = builder.equal(otherAnalysis.get(Analysis_.parent), analysis.get(Analysis_.id));

                    subquery.select(otherAnalysis.get(Analysis_.id.getName()));

                    subquery.where(predicateParent);

                    return builder.not(builder.exists(subquery));
                });


        List<Predicate> collect = Lists.newArrayList(predicateId, predicateNameFIR, predicateCodeFIR, predicateCodeIATA, predicateCodeLocal, predicateCurrent)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());

        criteria.where(builder.and(collect.toArray(new Predicate[collect.size()])));

        return currentSession().createQuery(criteria).getResultList();
    }

    @Override
    protected void preCreateActions(Analysis analysis, Analysis parent) {
        super.preCreateActions(analysis, parent);

        analysis.setParent(parent);
        analysis.setRegulation(parent.getRegulation());
        analysis.setStage(AnalysisStages.OBJECT);
        analysis.setStatus(AnalysisStatuses.INITIALIZED);
        analysis.setCreationDate(LocalDateTime.now(ZoneOffset.UTC));
        analysis.setEditionDate(LocalDateTime.now(ZoneOffset.UTC));
    }

    @Override
    protected void postCreateActions(Analysis analysis, Analysis parent) {
        super.postCreateActions(analysis, parent);

        createAnalysisCase(analysis, parent.getAnalysisCase().getAerodrome());
    }

    private void createAnalysisCase(Analysis analysis, Airport airport) {
        AnalysisCase analysisCase = AnalysisCase.builder()
                .id(analysis.getId())
                .aerodrome(airport)
                .analysis(analysis)
                .searchRadius(0.1D)
                .build();

        caseService.create(analysisCase, analysis);

        analysis.setAnalysisCase(analysisCase);
    }

    @Override
    protected void validateCreation(Analysis analysis, Analysis parent) {
        super.validateCreation(analysis, parent);

        if(checkAnyAnalysisOpen(parent.getAnalysisCase().getAerodrome()))
            throw new BusinessConstrainException("Cannot create a new analysis, because there is already an analysis in progress.");

    }

    private boolean checkAnyAnalysisOpen(Airport aerodrome) {

        CriteriaBuilder builder = currentSession().getCriteriaBuilder();

        CriteriaQuery<Analysis> criteria = builder.createQuery(Analysis.class);

        Root<Analysis> analysis = criteria.from(Analysis.class);

        Join<Object, Object> analysisCase = analysis.join(Analysis_.analysisCase.getName());

        Predicate predicateAirport = builder.equal(analysisCase.get(AnalysisCase_.aerodrome.getName()), aerodrome.getId());

        Predicate predicateStatus = builder.or(
                builder.equal(analysis.get(Analysis_.status.getName()), AnalysisStatuses.IN_PROGRESS),
                builder.equal(analysis.get(Analysis_.status.getName()), AnalysisStatuses.INITIALIZED)
        );

        criteria.where(predicateAirport, predicateStatus);

        return ! currentSession().createQuery(criteria).getResultList().isEmpty();
    }

}
