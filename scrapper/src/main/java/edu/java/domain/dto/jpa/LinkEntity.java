package edu.java.domain.dto.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import static jakarta.persistence.FetchType.EAGER;

@Getter
@Setter
@Entity
@Table(name = "link")
public class LinkEntity {
    @Id
    @Column(name = "url")
    private String url;

    @Column(name = "answer_count")
    private Integer answerCount;

    @Column(name = "star_count")
    private Integer starCount;

    @Column(name = "checked_date")
    private OffsetDateTime checkedDate;

    @Column(name = "updated_date")
    private OffsetDateTime updatedDate;

    @ManyToMany(mappedBy = "links", fetch = EAGER)
    private Set<ChatEntity> chats = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o instanceof LinkEntity link) {
            return url.equals(link.url)
                && answerCount.equals(link.answerCount)
                && starCount.equals(link.starCount)
                && checkedDate.equals(link.checkedDate)
                && updatedDate.equals(link.updatedDate);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, answerCount, starCount, checkedDate, updatedDate);
    }
}
