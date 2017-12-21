package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisSurface;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisSurface_;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14Surface;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import com.google.common.collect.Lists;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import spark.QueryParamsMap;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class AnalysisSurfaceService extends SigoService<AnalysisSurface, AnalysisCase> {

    @Inject
    public AnalysisSurfaceService(HibernateUtil util) {
        super(AnalysisSurface.class, util.getSessionFactory());
    }

    public Stream<AnalysisSurface> find(Long analysisCaseId, QueryParamsMap parameters) {
        CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        CriteriaQuery<AnalysisSurface> criteria = builder.createQuery(AnalysisSurface.class);
        Root<AnalysisSurface> analysisSurface = criteria.from(AnalysisSurface.class);

        Optional<Predicate> predicateCaseId = Optional
                .of(analysisCaseId)
                .map(v -> builder.equal(analysisSurface.get(AnalysisSurface_.analysisCase), v));

        Optional<Predicate> predicateDirection = Optional
                .ofNullable(parameters.get(AnalysisSurface_.direction.getName()).longValue())
                .map(v -> builder.equal(analysisSurface.get(AnalysisSurface_.direction), v));

        List<Predicate> collect = Lists.newArrayList(predicateCaseId, predicateDirection)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());

        criteria.where(builder.and(collect.toArray(new Predicate[collect.size()])));

        return currentSession().createQuery(criteria).getResultStream();
    }

    public SimpleFeature getFeature(AnalysisSurface analysisSurface) {

        return SimpleFeatureBuilder.build(
                getFeatureSchema(analysisSurface),
                new Object[]{
                        analysisSurface.getGeometry(),
                        ((ICAOAnnex14Surface)analysisSurface.getSurface()).getEnum().name(),
                        analysisSurface.getSurface().getName()
                },
                analysisSurface.getId().toString()
        );
    }

    private SimpleFeatureType getFeatureSchema(AnalysisSurface analysisSurface) {

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();

        tb.setName("Surface");
        tb.add("geom", Polygon.class, DefaultGeographicCRS.WGS84);
        tb.add("class", String.class);
        tb.add("name", String.class);

        //TODO agregar dinamicamente propiedades dependiendo del tipo de superficie

        return tb.buildFeatureType();
    }
}
