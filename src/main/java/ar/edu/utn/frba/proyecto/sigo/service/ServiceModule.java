package ar.edu.utn.frba.proyecto.sigo.service;

import ar.edu.utn.frba.proyecto.sigo.service.airport.*;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.*;
import ar.edu.utn.frba.proyecto.sigo.service.location.PoliticalLocationService;
import ar.edu.utn.frba.proyecto.sigo.service.location.RegionService;
import ar.edu.utn.frba.proyecto.sigo.service.object.*;
import ar.edu.utn.frba.proyecto.sigo.service.ols.OlsAnalystFactory;
import ar.edu.utn.frba.proyecto.sigo.service.regulation.OlsRuleICAOAnnex14Service;
import ar.edu.utn.frba.proyecto.sigo.service.regulation.OlsRuleService;
import ar.edu.utn.frba.proyecto.sigo.service.wizard.*;
import ar.edu.utn.frba.proyecto.sigo.translator.airport.AirportTranslator;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;

public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {

        bind(OlsRuleICAOAnnex14Service.class);
        bind(CatalogObjectService.class);
        bind(ObjectOwnerService.class);
        bind(PlacedObjectService.class);
        bind(PlacedObjectFeatureHelper.class);
        bind(PoliticalLocationService.class);
        bind(RegionService.class);
        bind(AnalysisCaseService.class);
        bind(AnalysisService.class);
        bind(AnalysisExceptionService.class);
        bind(AnalysisObjectService.class);
        bind(RunwayService.class);
        bind(RunwayTakeoffSectionService.class);
        bind(RunwayApproachSectionService.class);
        bind(RunwayDirectionService.class);
        bind(RunwayClassificationService.class);
        bind(AirportService.class);
        bind(AirportTranslator.class);
        bind(CatalogAirportService.class);
        bind(RunwayStripService.class);
        bind(AnalysisSurfaceService.class);
        bind(AnalysisObstacleService.class);
        bind(AnalysisAdverseEffectAspectService.class);
        bind(AnalysisAdverseEffectMitigationService.class);
        bind(AnalysisResultService.class);
        bind(TerrainLevelCurveService.class);
        bind(AnalysisExceptionRuleService.class);
        bind(AnalysisExceptionSurfaceService.class);
        bind(TrackSectionService.class);

        install(new FactoryModuleBuilder()
                .implement(OlsRuleService.class, OlsRuleICAOAnnex14Service.class)
                .build(OlsAnalystFactory.class)
        );


        Multibinder<WizardAnalysisStage> stagesBinder = Multibinder.newSetBinder(binder(), WizardAnalysisStage.class);
        stagesBinder.addBinding().to(WizardAnalysisStageObject.class);
        stagesBinder.addBinding().to(WizardAnalysisStageException.class);
        stagesBinder.addBinding().to(WizardAnalysisStageAnalysis.class);
        stagesBinder.addBinding().to(WizardAnalysisStageInform.class);
    }
}
