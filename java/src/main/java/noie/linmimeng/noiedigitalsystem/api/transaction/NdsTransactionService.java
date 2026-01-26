package noie.linmimeng.noiedigitalsystem.api.transaction;

import noie.linmimeng.noiedigitalsystem.api.asset.AssetId;
import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import noie.linmimeng.noiedigitalsystem.api.result.NdsResult;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

/**
 * NDS Transaction Service - 交易服務接口
 * 
 * <p>負責創建和執行交易。</p>
 * 
 * @since 2.0.0
 */
public interface NdsTransactionService {
    
    /**
     * 執行交易
     * 
     * @param transaction 交易事件
     * @return CompletableFuture 包含執行結果
     */
    CompletableFuture<NdsResult<Void>> execute(NdsTransaction transaction);
    
    /**
     * 創建並執行交易（便捷方法）
     * 
     * @param asset 資產 ID
     * @param delta 變更量
     * @param actor 發起者
     * @param consistency 一致性模式
     * @return CompletableFuture 包含執行結果
     */
    CompletableFuture<NdsResult<Void>> execute(
        AssetId asset,
        BigDecimal delta,
        NdsIdentity actor,
        ConsistencyMode consistency
    );
    
    /**
     * 轉賬（從源到目標）
     * 
     * @param asset 資產 ID
     * @param amount 金額
     * @param source 源身份
     * @param target 目標身份
     * @param reason 原因（可為 null）
     * @return CompletableFuture 包含執行結果
     */
    CompletableFuture<NdsResult<Void>> transfer(
        AssetId asset,
        BigDecimal amount,
        NdsIdentity source,
        NdsIdentity target,
        String reason
    );
}

