package com.nimain.antproject.core.domain.tags

import com.nimain.antproject.core.domain.common.exception.TagNotFoundException
import com.nimain.antproject.core.domain.tags.exception.TagAlreadyExistsException
import kotlinx.coroutines.flow.Flow

interface TagRepository {
    /**
     * Наблюдает за всеми тегами.
     * Порядок: по алфавиту без учёта регистра.
     */
    fun observeTags(): Flow<List<TagItem>>

    /**
     * Создаёт тег с названием [title].
     * Названия тегов уникальны без учёта регистра.
     * Пустой [title] — ошибка вызывающего кода: ввод должен проверять экран.
     *
     * @return идентификатор созданного тега.
     * @throws TagAlreadyExistsException если тег с таким названием уже есть.
     * @throws IllegalArgumentException если [title] пустой.
     */
    suspend fun createTag(title: String): TagId

    /**
     * Меняет название тега с [id] на [title].
     * Пустой [title] — ошибка вызывающего кода: ввод должен проверять экран.
     *
     * @throws TagNotFoundException если тега с [id] нет.
     * @throws TagAlreadyExistsException если другой тег с таким названием уже есть.
     * @throws IllegalArgumentException если [title] пустой.
     */
    suspend fun renameTag(
        id: TagId,
        title: String,
    )

    /**
     * Удаляет тег с [id] и снимает его со всех задач.
     * Если тега с [id] нет, ничего не делает.
     */
    suspend fun removeTag(id: TagId)
}
