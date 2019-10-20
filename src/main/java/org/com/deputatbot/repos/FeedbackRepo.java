package org.com.deputatbot.repos;

import org.com.deputatbot.domain.Feedback;
import org.com.deputatbot.domain.TypeFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepo extends JpaRepository<Feedback,Long> {

    List<Feedback> findAllByTypeFeedback(TypeFeedback typeFeedback);

}
