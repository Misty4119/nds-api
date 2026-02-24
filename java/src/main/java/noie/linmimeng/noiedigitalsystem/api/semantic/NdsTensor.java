package noie.linmimeng.noiedigitalsystem.api.semantic;

/**
 * [Index] NDS-JAVA-TENSOR-000
 * [Semantic] Immutable multi-dimensional numeric tensor for AI model integration.
 *
 * <p>[Constraint] Implementations must be immutable.</p>
 * <p>[Constraint] Must support at least one of: float array or quantized byte array.</p>
 *
 * @since 2.0.0
 */
public interface NdsTensor {

    /**
     * @return float array representation
     * @throws UnsupportedOperationException if this tensor does not support float format
     */
    float[] asFloatArray();

    /**
     * @return quantized byte array representation
     * @throws UnsupportedOperationException if this tensor does not support quantization
     */
    byte[] asQuantizedBytes();

    /**
     * @return shape array (e.g. [128] = 128-dim vector, [10, 20] = 10×20 matrix)
     */
    int[] dimensions();

    /** @return total element count (product of all dimension sizes) */
    default int elementCount() {
        int[] dims = dimensions();
        int count = 1;
        for (int dim : dims) {
            count *= dim;
        }
        return count;
    }

    /** @return true if {@link #asFloatArray()} is supported */
    default boolean supportsFloatArray() {
        try {
            asFloatArray();
            return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }

    /** @return true if {@link #asQuantizedBytes()} is supported */
    default boolean supportsQuantization() {
        try {
            asQuantizedBytes();
            return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }
}

