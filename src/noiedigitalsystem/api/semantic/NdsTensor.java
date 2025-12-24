package noie.linmimeng.noiedigitalsystem.api.semantic;

/**
 * NDS Tensor - 張量接口
 * 
 * <p>用於表示向量化的數據，支持 AI 模型處理。</p>
 * 
 * <p><b>憲法級規則：</b></p>
 * <ul>
 *   <li>必須支持浮點數組和量化字節數組</li>
 *   <li>必須支持多維度</li>
 *   <li>必須 Immutable</li>
 * </ul>
 * 
 * @since 2.0.0
 */
public interface NdsTensor {
    
    /**
     * 獲取浮點數組
     * 
     * @return 浮點數組（如果支持）
     * @throws UnsupportedOperationException 如果不支持浮點數組
     */
    float[] asFloatArray();
    
    /**
     * 獲取量化字節數組
     * 
     * @return 量化字節數組（如果支持）
     * @throws UnsupportedOperationException 如果不支持量化
     */
    byte[] asQuantizedBytes();
    
    /**
     * 獲取維度
     * 
     * @return 維度數組（如 [128] 表示 128 維向量，[10, 20] 表示 10x20 矩陣）
     */
    int[] dimensions();
    
    /**
     * 獲取元素總數
     * 
     * @return 元素總數
     */
    default int elementCount() {
        int[] dims = dimensions();
        int count = 1;
        for (int dim : dims) {
            count *= dim;
        }
        return count;
    }
    
    /**
     * 檢查是否支持浮點數組
     * 
     * @return true 如果支持
     */
    default boolean supportsFloatArray() {
        try {
            asFloatArray();
            return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }
    
    /**
     * 檢查是否支持量化
     * 
     * @return true 如果支持
     */
    default boolean supportsQuantization() {
        try {
            asQuantizedBytes();
            return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }
}

