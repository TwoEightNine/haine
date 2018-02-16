package global.msnthrp.haine.chat

import global.msnthrp.haine.model.Message
import global.msnthrp.haine.model.Sticker
import io.reactivex.processors.PublishProcessor

/**
 * Created by msnthrp on 28/01/18.
 */
object ChatBus {

    private val stickerProcessor: PublishProcessor<Sticker> = PublishProcessor.create()

    fun subscribeSticker(consumer: (Sticker) -> Unit) = stickerProcessor.subscribe(consumer)

    fun publishSticker(sticker: Sticker) {
        stickerProcessor.onNext(sticker)
    }

    private val messageProcessor: PublishProcessor<List<Message>> = PublishProcessor.create()

    fun subscribeMessage(consumer: (List<Message>) -> Unit) = messageProcessor.subscribe(consumer)

    fun publishMessage(messages: List<Message>) {
        messageProcessor.onNext(messages)
    }

    private val exchangeProcessor: PublishProcessor<String> = PublishProcessor.create()

    fun subscribeExchange(consumer: (String) -> Unit) = exchangeProcessor.subscribe(consumer)

    fun publishExchange(exchange: String) {
        exchangeProcessor.onNext(exchange)
    }

}