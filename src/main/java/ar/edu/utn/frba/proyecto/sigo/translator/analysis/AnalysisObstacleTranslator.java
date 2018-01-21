package ar.edu.utn.frba.proyecto.sigo.translator.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisObstacle;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisRestrictionTypes;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisSurface;
import ar.edu.utn.frba.proyecto.sigo.domain.object.ElevatedObject;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisObstacleDTO;
import ar.edu.utn.frba.proyecto.sigo.translator.SigoTranslator;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.vividsolutions.jts.geom.Coordinate;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class AnalysisObstacleTranslator extends SigoTranslator<AnalysisObstacle, AnalysisObstacleDTO> {

    @Inject
    public AnalysisObstacleTranslator(Gson objectMapper) {
        super(objectMapper, AnalysisObstacleDTO.class, AnalysisObstacle.class);
    }

    @Override
    public AnalysisObstacleDTO getAsDTO(AnalysisObstacle domain) {
        AnalysisObstacleDTO.AnalysisObstacleDTOBuilder builder = AnalysisObstacleDTO.builder();

        ElevatedObject placedObject = domain.getObject().getElevatedObject();
        Coordinate objectCoordinate = placedObject.getGeom().getCoordinate();

        builder
            .id(domain.getId())
            .caseId(domain.getAnalysisCase().getId())
            .objectId(placedObject.getId())
            .objectName(placedObject.getName())
            .objectType(placedObject.getType().ordinal())
            .coordinate(Lists.newArrayList(objectCoordinate.x, objectCoordinate.y))
            .objectHeight(domain.getObjectHeight())
            .restrictionHeight(domain.getRestrictionHeight())
            .penetration(domain.getPenetration())
            .restrictionId(domain.getRestriction().getId())
            .restrictionName(domain.getRestriction().getName())
            .restrictionTypeId(domain.getRestriction().getRestrictionType().ordinal());

        if(domain.getRestriction().getRestrictionType().equals(AnalysisRestrictionTypes.OBSTACLE_LIMITATION_SURFACE)){
            RunwayDirection direction = ((AnalysisSurface)domain.getRestriction()).getDirection();
            builder
                    .directionId(direction.getId())
                    .directionName(direction.getIdentifier());
        }

        Optional.ofNullable(domain.getResult())
                .ifPresent(r -> builder
                        .resultId(domain.getResult().getId())
                        .resultSummary(r.getSummary())
                );

        return builder.build();
    }

    @Override
    public AnalysisObstacle getAsDomain(AnalysisObstacleDTO analysisObstacleDTO) {
        throw new NotImplementedException();
    }
}
