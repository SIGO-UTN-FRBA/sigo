package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport_;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase_;
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
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class AnalysisCaseService extends SigoService <AnalysisCase, AnalysisCase> {

    @Inject
    public AnalysisCaseService(HibernateUtil hibernateUtil) {
        super(AnalysisCase.class, hibernateUtil.getSessionFactory());
    }

    public List<AnalysisCase> find(QueryParamsMap parameters) {

        CriteriaBuilder builder = currentSession().getCriteriaBuilder();

        CriteriaQuery<AnalysisCase> criteria = builder.createQuery(AnalysisCase.class);

        Root<AnalysisCase> analysisCase = criteria.from(AnalysisCase.class);

        Join<AnalysisCase, Airport> airport = analysisCase.join(AnalysisCase_.aerodrome);

        Optional<Predicate> predicate1 = Optional
                .ofNullable(parameters.get(Airport_.nameFIR.getName()).value())
                .map(v -> builder.like(airport.get(Airport_.nameFIR), String.format("%%%s%%",v)));

        Optional<Predicate> predicate2 = Optional
                .ofNullable(parameters.get(Airport_.codeFIR.getName()).value())
                .map(v -> builder.equal(airport.get(Airport_.codeFIR), v));

        Optional<Predicate> predicate3 = Optional
                .ofNullable(parameters.get(Airport_.codeIATA.getName()).value())
                .map(v -> builder.equal(airport.get(Airport_.codeIATA),v));

        List<Predicate> collect = Lists.newArrayList(predicate1, predicate2, predicate3)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());

        criteria.where(builder.and(collect.toArray(new Predicate[collect.size()])));

        return currentSession().createQuery(criteria).getResultList();
    }
}
