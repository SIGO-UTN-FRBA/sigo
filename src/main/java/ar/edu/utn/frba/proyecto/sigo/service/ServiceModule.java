package ar.edu.utn.frba.proyecto.sigo.service;

import ar.edu.utn.frba.proyecto.sigo.service.airport.AirportService;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.*;
import ar.edu.utn.frba.proyecto.sigo.service.object.CatalogObjectService;
import ar.edu.utn.frba.proyecto.sigo.service.object.PlacedObjectFeatureService;
import ar.edu.utn.frba.proyecto.sigo.translator.airport.AirportTranslator;
import ar.edu.utn.frba.proyecto.sigo.service.airport.CatalogAirportService;
import ar.edu.utn.frba.proyecto.sigo.service.airport.RunwayApproachSectionService;
import ar.edu.utn.frba.proyecto.sigo.translator.airport.RunwayApproachSectionTranslator;
import ar.edu.utn.frba.proyecto.sigo.service.airport.RunwayClassificationService;
import ar.edu.utn.frba.proyecto.sigo.translator.airport.RunwayClassificationTranslator;
import ar.edu.utn.frba.proyecto.sigo.service.airport.RunwayDirectionService;
import ar.edu.utn.frba.proyecto.sigo.translator.airport.RunwayDirectionTranslator;
import ar.edu.utn.frba.proyecto.sigo.service.airport.RunwayService;
import ar.edu.utn.frba.proyecto.sigo.service.airport.RunwayStripService;
import ar.edu.utn.frba.proyecto.sigo.service.airport.RunwayTakeoffSectionService;
import ar.edu.utn.frba.proyecto.sigo.translator.airport.RunwayTakeoffSectionTranslator;
import ar.edu.utn.frba.proyecto.sigo.translator.airport.RunwayTranslator;
import ar.edu.utn.frba.proyecto.sigo.translator.analysis.AnalysisCaseTranslator;
import ar.edu.utn.frba.proyecto.sigo.translator.analysis.AnalysisExceptionTranslator;
import ar.edu.utn.frba.proyecto.sigo.translator.analysis.AnalysisObjectTranslator;
import ar.edu.utn.frba.proyecto.sigo.translator.analysis.AnalysisTranslator;
import ar.edu.utn.frba.proyecto.sigo.service.location.PoliticalLocationService;
import ar.edu.utn.frba.proyecto.sigo.service.location.RegionService;
import ar.edu.utn.frba.proyecto.sigo.service.object.ObjectOwnerService;
import ar.edu.utn.frba.proyecto.sigo.service.object.PlacedObjectService;
import ar.edu.utn.frba.proyecto.sigo.service.ols.OlsAnalystFactory;
import ar.edu.utn.frba.proyecto.sigo.service.regulation.OlsRuleICAOAnnex14Service;
import ar.edu.utn.frba.proyecto.sigo.service.regulation.OlsRuleService;
import ar.edu.utn.frba.proyecto.sigo.service.wizard.WizardAnalysisStage;
import ar.edu.utn.frba.proyecto.sigo.service.wizard.WizardAnalysisStageAnalysis;
import ar.edu.utn.frba.proyecto.sigo.service.wizard.WizardAnalysisStageException;
import ar.edu.utn.frba.proyecto.sigo.service.wizard.WizardAnalysisStageInform;
import ar.edu.utn.frba.proyecto.sigo.service.wizard.WizardAnalysisStageObject;
import ar.edu.utn.frba.proyecto.sigo.translator.SimpleFeatureTranslator;
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
        bind(PlacedObjectFeatureService.class);
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
        bind(AnalysisResultReasonService.class);
        bind(AnalysisResultService.class);

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
