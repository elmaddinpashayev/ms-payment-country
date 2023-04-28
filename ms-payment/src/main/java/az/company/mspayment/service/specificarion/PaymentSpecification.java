package az.company.mspayment.service.specificarion;

import az.company.mspayment.model.entity.Payment;
import az.company.mspayment.model.request.PaymentCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RequiredArgsConstructor
public class PaymentSpecification implements Specification<Payment> {

    private final PaymentCriteria paymentCriteria;

    @Override
    public Predicate toPredicate(Root<Payment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicates = criteriaBuilder.conjunction();

        if (paymentCriteria.getAmountFrom() != null) {
            predicates = criteriaBuilder.and(predicates,
                    criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), paymentCriteria.getAmountFrom()));
        }

        if (paymentCriteria.getAmountTo() != null) {
            predicates = criteriaBuilder.and(predicates,
                    criteriaBuilder.lessThanOrEqualTo(root.get("amount"), paymentCriteria.getAmountTo()));
        }

        if (paymentCriteria.getDescription() != null && !paymentCriteria.getDescription().isEmpty()) {
            predicates= criteriaBuilder.and(predicates,
                    criteriaBuilder.like(root.get("description"), "%" + paymentCriteria.getDescription() + "%"));
        }

        return predicates;
    }
}
