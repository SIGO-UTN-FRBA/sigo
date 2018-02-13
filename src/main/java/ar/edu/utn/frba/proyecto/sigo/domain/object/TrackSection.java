package ar.edu.utn.frba.proyecto.sigo.domain.object;

import com.vividsolutions.jts.geom.LineString;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tbl_track_sections")
@NoArgsConstructor
@Data
public class TrackSection extends ElevatedObject<LineString>{

    @Builder
    public TrackSection(Long id, String name, Double heightAgl, Double heightAmls, LineString geom, TrackTypes subtype, Boolean verified) {
        super(id, name, heightAgl, heightAmls);
        this.geom = geom;
        this.subtype = subtype;
        this.verified = verified;
    }

    @Column
    protected Boolean verified;

    @Column
    private LineString geom;

    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "subtype_2")
    private TrackTypes subtype;

    @Override
    public ElevatedObjectTypes getType() {
        return ElevatedObjectTypes.TRACK_SECTION;
    }
}
