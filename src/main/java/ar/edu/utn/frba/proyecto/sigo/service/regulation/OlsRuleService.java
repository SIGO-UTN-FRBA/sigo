package ar.edu.utn.frba.proyecto.sigo.service.regulation;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.OlsRule;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.OlsRule_;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCategories;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayClassifications;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCodeNumbers;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14SurfaceConical;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14Surfaces;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.OlsRulesICAOAnnex14Spec_;
import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class OlsRuleService extends SigoService<OlsRule, OlsRule> {

    @Inject
    public OlsRuleService(HibernateUtil hibernateUtil) {
        super(OlsRule.class, hibernateUtil.getSessionFactory());
    }

    public List<OlsRule> getICAOAnnex14Rules(
            ICAOAnnex14Surfaces surface,
            ICAOAnnex14RunwayCodeNumbers codeNumber,
            ICAOAnnex14RunwayClassifications classification,
            ICAOAnnex14RunwayCategories category
    ) {

        CriteriaBuilder builder = currentSession().getCriteriaBuilder();

        CriteriaQuery<OlsRule> criteria = builder.createQuery(OlsRule.class);

        Root<OlsRule> rule = criteria.from(OlsRule.class);

        Join<Object, Object> icao = rule.join(OlsRule_.icaoRule.getName());

        Predicate predicate0 = builder.equal(icao.get(OlsRulesICAOAnnex14Spec_.surface.getName()), surface);
        Predicate predicate1 = builder.equal(icao.get(OlsRulesICAOAnnex14Spec_.runwayCodeNumber.getName()), codeNumber);
        Predicate predicate2 = builder.equal(icao.get(OlsRulesICAOAnnex14Spec_.runwayClassification.getName()), classification);
        Predicate predicate3 = builder.equal(icao.get(OlsRulesICAOAnnex14Spec_.runwayCategory.getName()), category);

        criteria.where(predicate0, predicate1,predicate2,predicate3);

        return currentSession().createQuery(criteria).getResultList();
    }

    public List<ICAOAnnex14Surfaces> getICAOAnnex14Surfaces(
            ICAOAnnex14RunwayClassifications classification,
            ICAOAnnex14RunwayCategories category,
            Boolean withRecommendations
    ){

        ArrayList<ICAOAnnex14Surfaces> surfaces = Lists.newArrayList(
                ICAOAnnex14Surfaces.CONICAL,
                ICAOAnnex14Surfaces.INNER_HORIZONTAL,
                ICAOAnnex14Surfaces.APPROACH,
                ICAOAnnex14Surfaces.TRANSITIONAL
        );

        switch (classification){

            case NON_INSTRUMENT:
                return surfaces;

            case NON_PRECISION_APPROACH:
                return surfaces;

            case PRECISION_APPROACH:

                ArrayList<ICAOAnnex14Surfaces> extras = Lists.newArrayList(
                        ICAOAnnex14Surfaces.INNER_APPROACH,
                        ICAOAnnex14Surfaces.INNER_TRANSITIONAL,
                        ICAOAnnex14Surfaces.BALKED_LANDING_SURFACE
                );

                switch (category){

                    case CATEGORY_I:
                        if(withRecommendations)
                            surfaces.addAll(extras);

                        return surfaces;

                    case CATEGORY_II:
                        surfaces.addAll(extras);
                        return surfaces;

                    case CATEGORY_III:
                        surfaces.addAll(extras);
                        return surfaces;

                    default:
                        throw new InvalidParameterException("ICAOAnnex14RunwayCategory");
                }

            default:
                throw new InvalidParameterException("ICAOAnnex14RunwayCategory");
        }
    }

    public ICAOAnnex14SurfaceConical getICAOAnnex14SurfaceConical(
            ICAOAnnex14RunwayCodeNumbers numberCode,
            ICAOAnnex14RunwayClassifications classification,
            ICAOAnnex14RunwayCategories category
    ){
        List<OlsRule> rules = this.getICAOAnnex14Rules(
                ICAOAnnex14Surfaces.CONICAL,
                numberCode,
                classification,
                category
        );

        return ICAOAnnex14SurfacesFactory.createConicalSurface(rules);
    }
}
