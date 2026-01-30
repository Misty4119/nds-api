package noie.linmimeng.noiedigitalsystem.api.projection;

import noie.linmimeng.noiedigitalsystem.api.asset.AssetId;
import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import noie.linmimeng.noiedigitalsystem.api.context.NdsContext;
import noie.linmimeng.noiedigitalsystem.api.event.NdsEvent;
import noie.linmimeng.noiedigitalsystem.api.result.NdsResult;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * NDS Query Service - 查詢服務接口（完整定義）
 * 
 * <p>負責狀態查詢和投影。</p>
 * 
 * <p><b>憲法級規則：</b></p>
 * <ul>
 *   <li>所有查詢都是異步的</li>
 *   <li>查詢結果來自事件投影，不是直接讀取狀態</li>
 *   <li>不得修改狀態</li>
 * </ul>
 * 
 * @since 2.0.0
 */
public interface NdsQueryService {
    
    /**
     * 查詢資產餘額
     * 
     * @param asset 資產 ID
     * @param identity 身份對象（可為 null，表示服務器資產）
     * @param context 上下文（可為 null）
     * @return CompletableFuture 包含餘額結果
     */
    CompletableFuture<NdsResult<BigDecimal>> queryBalance(
        AssetId asset,
        NdsIdentity identity,
        NdsContext context
    );
    
    /**
     * 查詢資產餘額（便捷方法，無上下文）
     * 
     * @param asset 資產 ID
     * @param identity 身份對象（可為 null）
     * @return CompletableFuture 包含餘額結果
     */
    default CompletableFuture<NdsResult<BigDecimal>> queryBalance(
        AssetId asset,
        NdsIdentity identity
    ) {
        return queryBalance(asset, identity, null);
    }
    
    /**
     * 查詢歷史事件
     * 
     * @param asset 資產 ID（可為 null，表示所有資產）
     * @param identity 身份對象（可為 null，表示所有身份）
     * @param startTime 開始時間（可為 null）
     * @param endTime 結束時間（可為 null）
     * @param limit 限制數量
     * @param offset 偏移量
     * @param context 上下文（可為 null）
     * @return CompletableFuture 包含事件列表結果
     */
    CompletableFuture<NdsResult<List<NdsEvent>>> queryHistory(
        AssetId asset,
        NdsIdentity identity,
        Instant startTime,
        Instant endTime,
        int limit,
        int offset,
        NdsContext context
    );
    
    /**
     * 查詢投影狀態
     * 
     * @param projectionId 投影 ID
     * @param context 上下文（可為 null）
     * @return CompletableFuture 包含投影結果
     */
    <T> CompletableFuture<NdsResult<T>> queryProjection(
        ProjectionId projectionId,
        NdsContext context
    );
    
    /**
     * 查詢投影狀態（便捷方法）
     * 
     * @param projectionId 投影 ID
     * @return CompletableFuture 包含投影結果
     */
    default <T> CompletableFuture<NdsResult<T>> queryProjection(ProjectionId projectionId) {
        return queryProjection(projectionId, null);
    }
    
    /**
     * 重放事件到指定時間點
     * 
     * @param projectionId 投影 ID
     * @param targetTime 目標時間點
     * @param context 上下文（可為 null）
     * @return CompletableFuture 包含重放後的狀態結果
     */
    <T> CompletableFuture<NdsResult<T>> replay(
        ProjectionId projectionId,
        Instant targetTime,
        NdsContext context
    );
    
    /**
     * 註冊投影
     * 
     * @param projection 投影實例
     * @return CompletableFuture 包含註冊結果
     */
    <T> CompletableFuture<NdsResult<Void>> registerProjection(NdsProjection<T> projection);
    
    /**
     * 取消註冊投影
     * 
     * @param projectionId 投影 ID
     * @return CompletableFuture 包含取消註冊結果
     */
    CompletableFuture<NdsResult<Void>> unregisterProjection(ProjectionId projectionId);
}

