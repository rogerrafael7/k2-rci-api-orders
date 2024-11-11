package k2.rci.api.orders.infra.shared.types;

import java.util.List;

public record PaginationResponse<T>(
        int page,
        int limit,
        int totalItems,
        int totalPages,
        Boolean hasNextPage,
        Boolean hasPreviousPage,
        List<T> data
) {
}
