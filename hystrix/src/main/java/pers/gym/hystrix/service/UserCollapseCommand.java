package pers.gym.hystrix.service;

import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCollapserKey;
import com.netflix.hystrix.HystrixCollapserProperties;
import com.netflix.hystrix.HystrixCommand;
import pers.gym.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>用户集成批量请求命令
 *
 * @author gym on 2023-05-20 11:41
 */
public class UserCollapseCommand extends HystrixCollapser<List<User>, User, Long> {

    private final HystrixService hystrixService;
    private final Long id;

    public UserCollapseCommand(HystrixService hystrixService, Long id) {
        super(HystrixCollapser.Setter
                .withCollapserKey(HystrixCollapserKey.Factory.asKey("userCollapseCommand"))
                // 仅限间隔200毫秒内的请求集成在一次，超过200毫秒的将在下一次集成请求
                .andCollapserPropertiesDefaults(HystrixCollapserProperties.Setter().withTimerDelayInMilliseconds(200)));
        this.hystrixService = hystrixService;
        this.id = id;
    }

    /**
     * 请求参数
     */
    @Override
    public Long getRequestArgument() {
        return id;
    }

    /**
     * 请求合并方法
     *
     * @param collapsedRequests {@code Collection<CollapsedRequest<ResponseType, RequestArgumentType>>} containing {@link CollapsedRequest} objects containing the arguments of each request collapsed in this batch.
     */
    @Override
    protected HystrixCommand<List<User>> createCommand(Collection<CollapsedRequest<User, Long>> collapsedRequests) {
        List<Long> ids = new ArrayList<>();
        for (CollapsedRequest<User, Long> collapsedRequest : collapsedRequests) {
            ids.add(collapsedRequest.getArgument());
        }
        return new UserBatchCommand(hystrixService, ids);
    }

    /**
     * 请求结果分发
     *
     * @param batchResponse     The {@code <BatchReturnType>} returned from the {@link HystrixCommand}{@code <BatchReturnType>} command created by {@link #createCommand}.
     *                          <p>
     * @param collapsedRequests {@code Collection<CollapsedRequest<ResponseType, RequestArgumentType>>} containing {@link CollapsedRequest} objects containing the arguments of each request collapsed in this batch.
     *                          <p>
     *                          The {@link CollapsedRequest#setResponse(Object)} or {@link CollapsedRequest#setException(Exception)} must be called on each {@link CollapsedRequest} in the Collection.
     */
    @Override
    protected void mapResponseToRequests(List<User> batchResponse, Collection<CollapsedRequest<User, Long>> collapsedRequests) {
        int count = 0;
        for (CollapsedRequest<User, Long> collapsedRequest : collapsedRequests) {
            collapsedRequest.setResponse(batchResponse.get(count++));
        }
    }
}