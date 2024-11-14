/*
 * This file is generated by jOOQ.
 */

package edu.java.domain.repository.jooq.generated.tables.records;

import edu.java.domain.repository.jooq.generated.tables.Link;
import jakarta.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;

/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class LinkRecord extends UpdatableRecordImpl<LinkRecord>
    implements Record5<String, Integer, Integer, LocalDateTime, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>LINK.URL</code>.
     */
    public void setUrl(@NotNull String value) {
        set(0, value);
    }

    /**
     * Getter for <code>LINK.URL</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 1000000000)
    @NotNull
    public String getUrl() {
        return (String) get(0);
    }

    /**
     * Setter for <code>LINK.STAR_COUNT</code>.
     */
    public void setStarCount(@Nullable Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>LINK.STAR_COUNT</code>.
     */
    @Nullable
    public Integer getStarCount() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>LINK.ANSWER_COUNT</code>.
     */
    public void setAnswerCount(@Nullable Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>LINK.ANSWER_COUNT</code>.
     */
    @Nullable
    public Integer getAnswerCount() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>LINK.UPDATED_DATE</code>.
     */
    public void setUpdatedDate(@NotNull LocalDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>LINK.UPDATED_DATE</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public LocalDateTime getUpdatedDate() {
        return (LocalDateTime) get(3);
    }

    /**
     * Setter for <code>LINK.CHECKED_DATE</code>.
     */
    public void setCheckedDate(@NotNull LocalDateTime value) {
        set(4, value);
    }

    /**
     * Getter for <code>LINK.CHECKED_DATE</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public LocalDateTime getCheckedDate() {
        return (LocalDateTime) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row5<String, Integer, Integer, LocalDateTime, LocalDateTime> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row5<String, Integer, Integer, LocalDateTime, LocalDateTime> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<String> field1() {
        return Link.LINK.URL;
    }

    @Override
    @NotNull
    public Field<Integer> field2() {
        return Link.LINK.STAR_COUNT;
    }

    @Override
    @NotNull
    public Field<Integer> field3() {
        return Link.LINK.ANSWER_COUNT;
    }

    @Override
    @NotNull
    public Field<LocalDateTime> field4() {
        return Link.LINK.UPDATED_DATE;
    }

    @Override
    @NotNull
    public Field<LocalDateTime> field5() {
        return Link.LINK.CHECKED_DATE;
    }

    @Override
    @NotNull
    public String component1() {
        return getUrl();
    }

    @Override
    @Nullable
    public Integer component2() {
        return getStarCount();
    }

    @Override
    @Nullable
    public Integer component3() {
        return getAnswerCount();
    }

    @Override
    @NotNull
    public LocalDateTime component4() {
        return getUpdatedDate();
    }

    @Override
    @NotNull
    public LocalDateTime component5() {
        return getCheckedDate();
    }

    @Override
    @NotNull
    public String value1() {
        return getUrl();
    }

    @Override
    @Nullable
    public Integer value2() {
        return getStarCount();
    }

    @Override
    @Nullable
    public Integer value3() {
        return getAnswerCount();
    }

    @Override
    @NotNull
    public LocalDateTime value4() {
        return getUpdatedDate();
    }

    @Override
    @NotNull
    public LocalDateTime value5() {
        return getCheckedDate();
    }

    @Override
    @NotNull
    public LinkRecord value1(@NotNull String value) {
        setUrl(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value2(@Nullable Integer value) {
        setStarCount(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value3(@Nullable Integer value) {
        setAnswerCount(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value4(@NotNull LocalDateTime value) {
        setUpdatedDate(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value5(@NotNull LocalDateTime value) {
        setCheckedDate(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord values(
        @NotNull String value1,
        @Nullable Integer value2,
        @Nullable Integer value3,
        @NotNull LocalDateTime value4,
        @NotNull LocalDateTime value5
    ) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached LinkRecord
     */
    public LinkRecord() {
        super(Link.LINK);
    }

    /**
     * Create a detached, initialised LinkRecord
     */
    @ConstructorProperties({"url", "starCount", "answerCount", "updatedDate", "checkedDate"})
    public LinkRecord(
        @NotNull String url,
        @Nullable Integer starCount,
        @Nullable Integer answerCount,
        @NotNull LocalDateTime updatedDate,
        @NotNull LocalDateTime checkedDate
    ) {
        super(Link.LINK);

        setUrl(url);
        setStarCount(starCount);
        setAnswerCount(answerCount);
        setUpdatedDate(updatedDate);
        setCheckedDate(checkedDate);
        resetChangedOnNotNull();
    }
}