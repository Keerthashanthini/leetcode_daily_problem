import java.util.*;

class Solution {
    public int[] lexicographicallySmallestArray(int[] nums, int limit) {
        int n = nums.length;
        
        // Union-Find (Disjoint Set Union) Helper Class
        class DSU {
            int[] parent, size;
            
            public DSU(int n) {
                parent = new int[n];
                size = new int[n];
                for (int i = 0; i < n; i++) {
                    parent[i] = i;
                    size[i] = 1;
                }
            }
            
            // Find with path compression
            public int find(int x) {
                if (parent[x] != x) {
                    parent[x] = find(parent[x]);  // Path compression
                }
                return parent[x];
            }
            
            // Union by size
            public void union(int x, int y) {
                int rootX = find(x);
                int rootY = find(y);
                if (rootX != rootY) {
                    if (size[rootX] > size[rootY]) {
                        parent[rootY] = rootX;
                        size[rootX] += size[rootY];
                    } else {
                        parent[rootX] = rootY;
                        size[rootY] += size[rootX];
                    }
                }
            }
        }
        
        // Step 1: Sort the array along with its indices for efficient union-find
        int[] numsCopy = Arrays.copyOf(nums, n);
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }

        // Sort indices based on the values in nums
        Arrays.sort(indices, (a, b) -> numsCopy[a] - numsCopy[b]);

        // Step 2: Initialize DSU structure
        DSU dsu = new DSU(n);

        // Step 3: Union consecutive elements if their absolute difference <= limit
        for (int i = 0; i < n - 1; i++) {
            int currentIdx = indices[i];
            int nextIdx = indices[i + 1];
            if (Math.abs(nums[currentIdx] - nums[nextIdx]) <= limit) {
                dsu.union(currentIdx, nextIdx);
            }
        }
        
        // Step 4: Group elements by their connected components
        Map<Integer, List<Integer>> componentMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            if (!componentMap.containsKey(root)) {
                componentMap.put(root, new ArrayList<>());
            }
            componentMap.get(root).add(i);
        }
        
        // Step 5: Sort values in each component and rebuild the result array
        int[] result = new int[n];
        for (List<Integer> indicesList : componentMap.values()) {
            List<Integer> values = new ArrayList<>();
            for (int idx : indicesList) {
                values.add(nums[idx]);
            }
            Collections.sort(values);

            int i = 0;
            for (int idx : indicesList) {
                result[idx] = values.get(i++);
            }
        }
        
        return result;
    }
}
