package ar.edu.utn.frba.proyecto.sigo.service.regulation;

import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14Surface;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceApproach;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceApproachFirstSection;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceApproachHorizontalSection;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceApproachSecondSection;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceBalkedLanding;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceConical;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceInnerApproach;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceInnerHorizontal;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceInnerTransitional;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceStrip;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceTakeoffClimb;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceTransitional;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14Surfaces;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.OlsRule;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCategories;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayClassifications;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCodeNumbers;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.OlsRuleICAOAnnex14;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.OlsRuleICAOAnnex14_;
import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import ar.edu.utn.frba.proyecto.sigo.service.ols.icao.ICAOAnnex14SurfaceDefinitionsHelper;
import com.google.common.collect.Lists;
import spark.QueryParamsMap;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Singleton
public class OlsRuleICAOAnnex14Service
        extends SigoService<OlsRuleICAOAnnex14, OlsRule>
        implements OlsRuleService
{

    @Inject
    public OlsRuleICAOAnnex14Service(HibernateUtil hibernateUtil) {
        super(OlsRuleICAOAnnex14.class, hibernateUtil.getSessionFactory());
    }
    
    private List<OlsRuleICAOAnnex14> find(
            ICAOAnnex14Surfaces surface,
            ICAOAnnex14RunwayCodeNumbers codeNumber,
            ICAOAnnex14RunwayClassifications classification,
            ICAOAnnex14RunwayCategories category
    ) {

        CriteriaBuilder builder = currentSession().getCriteriaBuilder();

        CriteriaQuery<OlsRuleICAOAnnex14> criteria = builder.createQuery(OlsRuleICAOAnnex14.class);

        Root<OlsRuleICAOAnnex14> icao = criteria.from(OlsRuleICAOAnnex14.class);

        Optional<Predicate> predicateSurface = Optional
                .ofNullable(surface)
                .map(v -> builder.equal(icao.get(OlsRuleICAOAnnex14_.surface.getName()), surface));
        Optional<Predicate> predicateCode = Optional
                .ofNullable(codeNumber)
                .map(v -> builder.equal(icao.get(OlsRuleICAOAnnex14_.runwayCodeNumber.getName()), codeNumber));
        Optional<Predicate> predicateClassification = Optional
                .ofNullable(classification)
                .map(v-> builder.equal(icao.get(OlsRuleICAOAnnex14_.runwayClassification.getName()), classification));
        Optional<Predicate> predicateCategory = Optional
                .ofNullable(category)
                .map(v-> builder.equal(icao.get(OlsRuleICAOAnnex14_.runwayCategory.getName()), category));


        List<Predicate> collect = Lists.newArrayList(predicateSurface, predicateCode, predicateClassification, predicateCategory)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());

        criteria.where(builder.and(collect.toArray(new Predicate[collect.size()])));

        return currentSession().createQuery(criteria).getResultList();
    }

    public List<OlsRuleICAOAnnex14> find(QueryParamsMap params) {
        return this.find(
                Optional.ofNullable(params.get("surface")).map(v -> ICAOAnnex14Surfaces.values()[v.integerValue()]).orElse(null),
                Optional.ofNullable(params.get("number")).map(v -> ICAOAnnex14RunwayCodeNumbers.values()[v.integerValue()]).orElse(null),
                Optional.ofNullable(params.get("classification")).map(v -> ICAOAnnex14RunwayClassifications.values()[v.integerValue()]).orElse(null),
                Optional.ofNullable(params.get("category")).map(v -> ICAOAnnex14RunwayCategories.values()[v.integerValue()]).orElse(null)
        );
    }

    public List<ICAOAnnex14Surfaces> getCatalogOfSurfaces(){
        return Lists.newArrayList(ICAOAnnex14Surfaces.values());
    }

    public List<ICAOAnnex14Surfaces> getCatalogOfSurfaces(
            ICAOAnnex14RunwayClassifications classification,
            ICAOAnnex14RunwayCategories category,
            ICAOAnnex14RunwayCodeNumbers number,
            Boolean withRecommendations
    ){

        ArrayList<ICAOAnnex14Surfaces> surfaces = Lists.newArrayList(
                ICAOAnnex14Surfaces.STRIP,
                ICAOAnnex14Surfaces.CONICAL,
                ICAOAnnex14Surfaces.INNER_HORIZONTAL,
                ICAOAnnex14Surfaces.APPROACH,
                ICAOAnnex14Surfaces.APPROACH_FIRST_SECTION,
                ICAOAnnex14Surfaces.TRANSITIONAL,
                ICAOAnnex14Surfaces.TAKEOFF_CLIMB
        );

        switch (classification){

            case NON_INSTRUMENT:
                return surfaces;

            case NON_PRECISION_APPROACH:

                if(ICAOAnnex14RunwayCodeNumbers.THREE == number || ICAOAnnex14RunwayCodeNumbers.FOUR == number){
                    surfaces.add(ICAOAnnex14Surfaces.APPROACH_SECOND_SECTION);
                    surfaces.add(ICAOAnnex14Surfaces.APPROACH_HORIZONTAL_SECTION);
                }

                return surfaces;

            case PRECISION_APPROACH:

                surfaces.add(ICAOAnnex14Surfaces.APPROACH_SECOND_SECTION);
                surfaces.add(ICAOAnnex14Surfaces.APPROACH_HORIZONTAL_SECTION);

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
        List<OlsRuleICAOAnnex14> rules = this.find(
                ICAOAnnex14Surfaces.CONICAL,
                numberCode,
                classification,
                category
        );

        return new ICAOAnnex14SurfaceDefinitionsHelper().createConicalSurface(rules);
    }

    public ICAOAnnex14SurfaceApproach getICAOAnnex14SurfaceApproach(
            ICAOAnnex14RunwayCodeNumbers numberCode,
            ICAOAnnex14RunwayClassifications classification,
            ICAOAnnex14RunwayCategories category
    ){
        List<OlsRuleICAOAnnex14> rules = this.find(
                ICAOAnnex14Surfaces.APPROACH,
                numberCode,
                classification,
                category
        );

        return new ICAOAnnex14SurfaceDefinitionsHelper().createApproachSurface(rules);
    }

    public ICAOAnnex14SurfaceInnerApproach getICAOAnnex14SurfaceInnerApproach(
            ICAOAnnex14RunwayCodeNumbers numberCode,
            ICAOAnnex14RunwayClassifications classification,
            ICAOAnnex14RunwayCategories category
    ) {
        List<OlsRuleICAOAnnex14> rules = this.find(
                ICAOAnnex14Surfaces.INNER_APPROACH,
                numberCode,
                classification,
                category
        );

        return new ICAOAnnex14SurfaceDefinitionsHelper().createInnerApproachSurface(rules);
    }

    public ICAOAnnex14SurfaceInnerHorizontal getICAOAnnex14SurfaceInnerHorizontal(
            ICAOAnnex14RunwayCodeNumbers numberCode,
            ICAOAnnex14RunwayClassifications classification,
            ICAOAnnex14RunwayCategories category
    ) {
        List<OlsRuleICAOAnnex14> rules = this.find(
                ICAOAnnex14Surfaces.INNER_HORIZONTAL,
                numberCode,
                classification,
                category
        );

        return new ICAOAnnex14SurfaceDefinitionsHelper().createInnerHorizontalSurface(rules);
    }


    public ICAOAnnex14SurfaceApproachFirstSection getICAOAnnex14SurfaceApproachFirstSection(
            ICAOAnnex14RunwayCodeNumbers numberCode,
            ICAOAnnex14RunwayClassifications classification,
            ICAOAnnex14RunwayCategories category
    ) {
        List<OlsRuleICAOAnnex14> rules = this.find(
                ICAOAnnex14Surfaces.APPROACH_FIRST_SECTION,
                numberCode,
                classification,
                category
        );

        return new ICAOAnnex14SurfaceDefinitionsHelper().createApproachFirstSectionSurface(rules);
    }

    public ICAOAnnex14SurfaceApproachSecondSection getICAOAnnex14SurfaceApproachSecondSection(
            ICAOAnnex14RunwayCodeNumbers numberCode,
            ICAOAnnex14RunwayClassifications classification,
            ICAOAnnex14RunwayCategories category
    ) {
        List<OlsRuleICAOAnnex14> rules = this.find(
                ICAOAnnex14Surfaces.APPROACH_SECOND_SECTION,
                numberCode,
                classification,
                category
        );

        return new ICAOAnnex14SurfaceDefinitionsHelper().createApproachSecondSectionSurface(rules);
    }

    public ICAOAnnex14SurfaceApproachHorizontalSection getICAOAnnex14SurfaceApproachHorizontalSection(
            ICAOAnnex14RunwayCodeNumbers numberCode,
            ICAOAnnex14RunwayClassifications classification,
            ICAOAnnex14RunwayCategories category
    ) {
        List<OlsRuleICAOAnnex14> rules = this.find(
                ICAOAnnex14Surfaces.APPROACH_HORIZONTAL_SECTION,
                numberCode,
                classification,
                category
        );

        return new ICAOAnnex14SurfaceDefinitionsHelper().createApproachHorizontalSectionSurface(rules);
    }

    public ICAOAnnex14SurfaceTransitional getICAOAnnex14SurfaceTransitional(
            ICAOAnnex14RunwayCodeNumbers numberCode,
            ICAOAnnex14RunwayClassifications classification,
            ICAOAnnex14RunwayCategories category
    ) {
        List<OlsRuleICAOAnnex14> rules = this.find(
                ICAOAnnex14Surfaces.TRANSITIONAL,
                numberCode,
                classification,
                category
        );

        return new ICAOAnnex14SurfaceDefinitionsHelper().createTransitionalSurface(rules);
    }

    public ICAOAnnex14SurfaceInnerTransitional getICAOAnnex14SurfaceInnerTransitional(
            ICAOAnnex14RunwayCodeNumbers numberCode,
            ICAOAnnex14RunwayClassifications classification,
            ICAOAnnex14RunwayCategories category
    ) {
        List<OlsRuleICAOAnnex14> rules = this.find(
                ICAOAnnex14Surfaces.INNER_TRANSITIONAL,
                numberCode,
                classification,
                category
        );

        return new ICAOAnnex14SurfaceDefinitionsHelper().createInnerTransitionalSurface(rules);
    }

    public ICAOAnnex14SurfaceBalkedLanding getICAOAnnex14SurfaceBalkedLanding(
            ICAOAnnex14RunwayCodeNumbers numberCode,
            ICAOAnnex14RunwayClassifications classification,
            ICAOAnnex14RunwayCategories category
    ) {
        List<OlsRuleICAOAnnex14> rules = this.find(
                ICAOAnnex14Surfaces.BALKED_LANDING_SURFACE,
                numberCode,
                classification,
                category
        );

        return new ICAOAnnex14SurfaceDefinitionsHelper().createBalkedLandingSurface(rules);
    }

    public ICAOAnnex14SurfaceTakeoffClimb getICAOAnnex14SurfaceTakeoffClimb(
            ICAOAnnex14RunwayCodeNumbers numberCode,
            ICAOAnnex14RunwayClassifications classification,
            ICAOAnnex14RunwayCategories category
    ) {
        List<OlsRuleICAOAnnex14> rules = this.find(
                ICAOAnnex14Surfaces.TAKEOFF_CLIMB,
                numberCode,
                classification,
                null
        );

        return new ICAOAnnex14SurfaceDefinitionsHelper().createTakeoffClimbSurface(rules);
    }

    public ICAOAnnex14SurfaceStrip getICAOAnnex14SurfaceStrip(
            ICAOAnnex14RunwayCodeNumbers numberCode,
            ICAOAnnex14RunwayClassifications classification,
            ICAOAnnex14RunwayCategories category
    ) {
        List<OlsRuleICAOAnnex14> rules = this.find(
                ICAOAnnex14Surfaces.STRIP,
                numberCode,
                classification,
                category
        );

        return new ICAOAnnex14SurfaceDefinitionsHelper().createStripSurface(rules);
    }

    public List<ICAOAnnex14Surface> getSurfaces(
            ICAOAnnex14RunwayClassifications classification,
            ICAOAnnex14RunwayCategories category,
            ICAOAnnex14RunwayCodeNumbers number,
            Boolean withRecommendations
    ){
        return this.getCatalogOfSurfaces(classification,category,number, withRecommendations)
                .stream()
                .map(c -> getSurface(c, number, classification, category))
                .collect(Collectors.toList());
    }

    public ICAOAnnex14Surface getSurface(ICAOAnnex14Surfaces paramSurface, ICAOAnnex14RunwayCodeNumbers paramNumberCode, ICAOAnnex14RunwayClassifications paramClassification, ICAOAnnex14RunwayCategories paramCategory) {
        
        switch (paramSurface){

            case STRIP:
                return this.getICAOAnnex14SurfaceStrip(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
            case CONICAL:
                return this.getICAOAnnex14SurfaceConical(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
            case INNER_HORIZONTAL:
                return this.getICAOAnnex14SurfaceInnerHorizontal(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
            case INNER_APPROACH:
                return this.getICAOAnnex14SurfaceInnerApproach(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
            case APPROACH:
                return this.getICAOAnnex14SurfaceApproach(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
            case APPROACH_FIRST_SECTION:
                return this.getICAOAnnex14SurfaceApproachFirstSection(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
            case APPROACH_SECOND_SECTION:
                return this.getICAOAnnex14SurfaceApproachSecondSection(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
            case APPROACH_HORIZONTAL_SECTION:
                return this.getICAOAnnex14SurfaceApproachHorizontalSection(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
            case TRANSITIONAL:
                return this.getICAOAnnex14SurfaceTransitional(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
            case INNER_TRANSITIONAL:
                return this.getICAOAnnex14SurfaceInnerTransitional(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
            case BALKED_LANDING_SURFACE:
                return this.getICAOAnnex14SurfaceBalkedLanding(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
            case TAKEOFF_CLIMB:
                return this.getICAOAnnex14SurfaceTakeoffClimb(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
        }
        
        return null;
    }
}
