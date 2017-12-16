package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObject;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulations;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Entity
@Table(name = "public.tbl_analysis_cases")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class AnalysisCase extends SigoDomain {
    @Id
    @Column(name = "case_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_id")
    private Analysis analysis;

    @ManyToOne
    @JoinColumn(name = "aerodrome_id")
    private Airport aerodrome;

    @OneToMany(mappedBy = "analysisCase", cascade = CascadeType.ALL)
    private List<AnalysisObject> objects = Lists.newArrayList();

    @OneToMany(mappedBy="analysisCase", cascade = CascadeType.ALL)
    private Set<AnalysisException> exceptions = Sets.newHashSet();

    @Column(name="search_radius")
    private Double searchRadius;

    @OneToMany(mappedBy = "analysisCase", cascade = CascadeType.ALL)
    private List<AnalysisSurface> surfaces = Lists.newArrayList();

    @OneToMany(mappedBy = "analysisCase", cascade = CascadeType.ALL)
    private Set<AnalysisObstacle> obstacles = Sets.newHashSet();


    public String toString(){

        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("airport", aerodrome.getId())
                .add("analysis", analysis.getId())
                .toString();
    }

    public Regulations getRegulation(){
       return this.getAerodrome().getRegulation();
   }

    public Boolean isObjectAnalyzed(PlacedObject o) {
        return this.getObjects()
                .stream()
                .anyMatch(r -> r.getIncluded() && Objects.equals(r.getPlacedObject().getId(), o.getId()));
    }

    public Stream<AnalysisExceptionRule> getRuleExceptions(){
        return this.getExceptions()
                .stream()
                .filter(s -> s.getType().equals(AnalysisExceptions.RULE))
                .map(s -> (AnalysisExceptionRule)s);
    }

    public Stream<AnalysisExceptionSurface> getSurfaceExceptions(){
        return this.getExceptions()
                .stream()
                .filter(s -> s.getType().equals(AnalysisExceptions.SURFACE))
                .map(s -> (AnalysisExceptionSurface)s);
    }
}
