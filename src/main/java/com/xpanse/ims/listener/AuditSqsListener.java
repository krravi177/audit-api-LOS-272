package com.xpanse.ims.listener;

import org.springframework.stereotype.Component;
import com.xpanse.ims.service.SqsService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import software.amazon.awssdk.services.sqs.model.Message;

@Component
public class AuditSqsListener {

	private final SqsService sqsService;

	public AuditSqsListener(SqsService sqsService) {
		this.sqsService = sqsService;
	}

	@SqsListener("${aws.sqs.queue}")
	public void listen(Message message) {
		sqsService.postMessage(message);
	}
}
