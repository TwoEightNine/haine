package global.msnthrp.messenger.chat

import global.msnthrp.messenger.model.Sticker
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

}