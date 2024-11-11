package k2.rci.api.orders.infra.shared.types;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultPaginationRequest implements PaginationRequest {
    @QueryParam("page")
    @DefaultValue("1")
    private int page;

    @QueryParam("limit")
    @DefaultValue("100")
    private int limit;
}