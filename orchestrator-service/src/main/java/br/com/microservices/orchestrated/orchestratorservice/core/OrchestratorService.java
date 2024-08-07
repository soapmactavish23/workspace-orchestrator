package br.com.microservices.orchestrated.orchestratorservice.core;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.com.microservices.orchestrated.orchestratorservice.core.dto.Event;
import br.com.microservices.orchestrated.orchestratorservice.core.dto.History;
import br.com.microservices.orchestrated.orchestratorservice.core.enums.EEventSource;
import br.com.microservices.orchestrated.orchestratorservice.core.enums.ESagaStatus;
import br.com.microservices.orchestrated.orchestratorservice.core.enums.ETopics;
import br.com.microservices.orchestrated.orchestratorservice.core.producer.SagaOrchestratorProducer;
import br.com.microservices.orchestrated.orchestratorservice.core.saga.SagaExecutionController;
import br.com.microservices.orchestrated.orchestratorservice.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class OrchestratorService {

	private final JsonUtil jsonUtil;
	private final SagaOrchestratorProducer producer;
	private final SagaExecutionController sagaExecutionController;

	public void startSaga(Event event) {
		event.setSource(EEventSource.ORCHESTRATOR);
		event.setStatus(ESagaStatus.SUCCESS);
		var topic = getTopic(event);
		log.info("SAGA STARTED!");
		addHistory(event, "Saga started!");
		sendToProducerWithTopic(event, topic);
	}

	public void finishSagaSuccess(Event event) {
		event.setSource(EEventSource.ORCHESTRATOR);
		event.setStatus(ESagaStatus.SUCCESS);
		log.info("SAGA FINISHED SUCCESSFULLY FOR EVENT {}!", event.getId());
		addHistory(event, "Saga finished successfully!");
		notifyFinishedSaga(event);
	}

	public void finishSagaFail(Event event) {
		event.setSource(EEventSource.ORCHESTRATOR);
		event.setStatus(ESagaStatus.FAIL);
		log.info("SAGA FINISHED WITH ERRORS FOR EVENT {}!", event.getId());
		addHistory(event, "Saga finished with errors!");
		notifyFinishedSaga(event);
	}

	public void continueSaga(Event event) {
		var topic = getTopic(event);
		log.info("SAGA CONTINUING FOR EVENT {}", event.getId());
		sendToProducerWithTopic(event, topic);
	}
	
	private ETopics getTopic(Event event) {
		return sagaExecutionController.getNextTopic(event);
	}
	
	private void addHistory(Event event, String message) {
		var history = History.builder().source(event.getSource()).status(event.getStatus()).message(message)
				.createdAt(LocalDateTime.now()).build();
		event.addToHistory(history);
	}
	
	private void sendToProducerWithTopic(Event event, ETopics topic) {
		producer.sendEvent(jsonUtil.toJson(event), topic.getTopic());
	}
	
	private void notifyFinishedSaga(Event event) {
		producer.sendEvent(jsonUtil.toJson(event), ETopics.NOTIFY_ENDING.getTopic());
	}

}
