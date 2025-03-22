import java.util.Scanner;

// Class to represent a memory block
class MemoryBlock {
    int blockSize;
    int startAddress;
    int endAddress;
    String status; // "allocated" or "free"
    String processID; // "Null" if free
    int internalFragmentation; // Internal Fragmentation

    // Constructor to initialize a memory block
    public MemoryBlock(int startAddress, int blockSize) {
        this.startAddress = startAddress;
        this.blockSize = blockSize;
        this.endAddress = startAddress + blockSize - 1;
        this.status = "free";
        this.processID = "Null";
        this.internalFragmentation = 0;
    }

    // Free the block
    public void deallocate() {
        this.status = "free";
        this.processID = "Null";
        this.internalFragmentation = 0;
    }

    // Display block information
    public void printBlock(int index) {
        System.out.printf("%-8d %-7d %-8d %-7d %-8s %-8s %-5d\n",
                index, startAddress, endAddress, blockSize, status, processID, internalFragmentation);
    }
}

// Class containing memory allocation algorithms
class Algorithms {
    // First-Fit already implemented as example
    public static boolean firstFit(MemoryBlock[] memoryBlocks, String processID, int processSize, int strategy) {
        for (MemoryBlock block : memoryBlocks) {
            if (block.status.equals("free") && block.blockSize >= processSize) {
                block.status = "allocated";
                block.processID = processID;
                calculateFragmentation(block, strategy, processSize);
                System.out.printf("%s Allocated at address %d, and the internal fragmentation is %d\n",
                        processID, block.startAddress, block.internalFragmentation);
                return true;
            }
        }
        return false;
    }

    // Best-Fit Algorithm
    public static boolean bestFit(MemoryBlock[] memoryBlocks, String processID, int processSize, int strategy) {
        MemoryBlock bestBlock = null;
        int minSize = Integer.MAX_VALUE;

        for (MemoryBlock block : memoryBlocks) {
            if (block.status.equals("free") && block.blockSize >= processSize && block.blockSize < minSize) {
                bestBlock = block;
                minSize = block.blockSize;
            }
        }

        if (bestBlock != null) {
            bestBlock.status = "allocated";
            bestBlock.processID = processID;
            calculateFragmentation(bestBlock, strategy, processSize);
            System.out.printf("%s Allocated at address %d, and the internal fragmentation is %d\n",
                    processID, bestBlock.startAddress, bestBlock.internalFragmentation);
            return true;
        }
        return false;
    }

    // TODO: Implement the Worst-Fit Algorithm here.
    // It should allocate the process to the largest free block available in the memoryBlocks array.
    //public static boolean worstFit(MemoryBlock[] memoryBlocks, String processID, int processSize, int strategy)


    // Calculate internal fragmentation
    public static void calculateFragmentation(MemoryBlock block, int strategy, int processSize) {
        block.internalFragmentation = block.blockSize - processSize;
    }
}

public class MemoryInitialization {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize memory blocks
        System.out.print("Enter the number of memory blocks (M): ");
        int M = scanner.nextInt();
        MemoryBlock[] memoryBlocks = new MemoryBlock[M];
        int currentStartAddress = 0;
        for (int i = 0; i < M; i++) {
            System.out.print("Enter size of block " + (i + 1) + " in KB: ");
            int blockSize = scanner.nextInt();
            memoryBlocks[i] = new MemoryBlock(currentStartAddress, blockSize);
            currentStartAddress += blockSize;
        }

        // Ask for allocation strategy
        System.out.print("Enter allocation strategy (1 for first-fit, 2 for best-fit, 3 for worst-fit): ");
        int strategy = scanner.nextInt();

        // Handle user input for memory operations
        while (true) {
            System.out.println("1) Allocate memory blocks");
            System.out.println("2) Deallocate memory blocks");
            System.out.println("3) Print report about the current state of memory and internal Fragmentation");
            System.out.println("4) Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1: // Allocate memory block
                    System.out.print("Enter process ID: ");
                    String processID = scanner.next();
                    System.out.print("Enter process size in KB: ");
                    int processSize = scanner.nextInt();

                    boolean allocated = false;
                    if (strategy == 1) {
                        allocated = Algorithms.firstFit(memoryBlocks, processID, processSize, strategy);
                    } else if (strategy == 2) {
                        allocated = Algorithms.bestFit(memoryBlocks, processID, processSize, strategy);
                    } // Add else if for worstFit when implemented

                    if (!allocated) {
                        System.out.println("Error: No sufficient memory available for process " + processID);
                    }
                    break;

                case 2: // Deallocate memory block
                    System.out.print("Enter process ID to deallocate: ");
                    String deallocateID = scanner.next();
                    boolean found = false;
                    for (MemoryBlock block : memoryBlocks) {
                        if (block.status.equals("allocated") && block.processID.equals(deallocateID)) {
                            block.deallocate();
                            System.out.println("Process " + deallocateID + " deallocated.");
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        System.out.println("Error: Process ID not found.");
                    }
                    break;

                case 3: // Print memory status report
                    System.out.println("Memory Blocks Status:");
                    for (int i = 0; i < M; i++) {
                        memoryBlocks[i].printBlock(i);
                    }
                    break;

                case 4: // Exit program
                    System.out.println("Exiting program...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice! Please enter a valid option.");
            }
        }
    }
}

