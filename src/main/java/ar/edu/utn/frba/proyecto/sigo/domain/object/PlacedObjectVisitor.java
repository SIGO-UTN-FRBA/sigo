package ar.edu.utn.frba.proyecto.sigo.domain.object;

public interface PlacedObjectVisitor<T> {

    T visitPlacedObjectBuilding(PlacedObjectBuilding building);

    T visitPlacedObjectIndividual(PlacedObjectIndividual individual);

    T visitPlacedObjectOverheadWire(PlacedObjectOverheadWire wire);
}
