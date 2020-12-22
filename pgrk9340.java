//Name: Rohit Thole
//UCID: 31509340

import java.util.*;
import java.io.*;
import java.lang.*;
import static java.lang.Math.*;

public class pgrk9340 {

        int iteration;
        int initial_val;
        String filename;
        int n;      // number of vertices in the graph
        int m;      // number of edges in the graph
        int[][] Z;  // adjacency matrix 
        final double d = 0.85;
        final double errorrate = 0.00001;
        double[] src_db;
        int[] C; 

    pgrk9340() {} //default constructor

    pgrk9340(int iteration, int initial_val, String filename)     // 3 argument constructor to initialize class variables with provided command line arguments
    {
        this.iteration = iteration;
        this.initial_val = initial_val;
        this.filename = filename;
        int p = 0;
        int q = 0;
        try {        
            Scanner scanner = new Scanner(new File(filename));
            n = scanner.nextInt();
            m = scanner.nextInt();
            
            //Adjacency matrix representation of graph
            Z = new int[n][n];
            for(int i = 0; i < n; i++)
             for(int j = 0; j < n; j++)
               Z[i][j] = 0;
            
            while(scanner.hasNextInt())
            {
                p = scanner.nextInt();
                q = scanner.nextInt();
                Z[p][q] = 1;
            }
            

            C = new int[n];
            for(int i = 0; i < n; i++) {
                C[i] = 0;
                for(int j = 0; j < n; j++) {
                    C[i] += Z[i][j];
                }
            }

            src_db = new double[n];
            switch(initial_val) {
            case 0:
              for(int i = 0; i < n; i++) {
                src_db[i] = 0;
              }
              break;
            case 1:
              for(int i = 0; i < n; i++) {
                src_db[i] = 1;
              }
              break;
            case -1:
              for(int i =0; i < n; i++) {
                src_db[i] = 1.0/n;
              }
              break;
            case -2:
              for(int i =0; i < n; i++) {
                src_db[i] = 1.0/Math.sqrt(n);
              }
              break;
            }
        }
        catch(FileNotFoundException abc)
        {
        }
    }
    
    public static void main(String[] args)
    {
        if(args.length != 3) {
            System.out.println("");
            return;
        }
        //command line arguments
        int iterations = Integer.parseInt(args[0]);
        int initialvalue = Integer.parseInt(args[1]);
        String filename = args[2];

        if( !(initialvalue >= -2 && initialvalue <= 1) ) {
            System.out.println("InitialValue should be -2,-1,0,1");
            return;
        }

        pgrk9340 pr = new pgrk9340(iterations, initialvalue, filename);

        pr.pgrk_Algo();
    }

    boolean is_Converged(double[] src, double[] target)
    {
        for(int i = 0; i < n; i++)
        {
          if ( abs(src[i] - target[i]) > errorrate )
            return false;
        }
        return true;
    }

    void pgrk_Algo() {
        double[] D = new double[n];
        boolean flag = true;
 
        if(n > 10) {
            iteration = 0;
            for(int i =0; i < n; i++) {
              src_db[i] = 1.0/n;
            }
            int i = 0;
          do {
               if(flag == true)
               {
                  flag = false;
               }
               else
               {
                 for(int l = 0; l < n; l++) {
                   src_db[l] = D[l];
                 }
               }
               for(int l = 0; l < n; l++) {
                 D[l] = 0.0;
               }

               for(int j = 0; j < n; j++) {
                 for(int k = 0; k < n; k++)
                 {
                    if(Z[k][j] == 1) {
                        D[j] += src_db[k]/C[k];
                    }
                 }
               }

               //Compute and print pagerank of all pages
               for(int l = 0; l < n; l++) {
                 D[l] = d*D[l] + (1-d)/n;
               }
               i++;
             } while (is_Converged(src_db, D) != true);

             // print pageranks at the stopping iteration 
             System.out.println("Iter: " + i);
             for(int l = 0 ; l < n; l++) {
                 System.out.printf("P[" + l + "] = %.6f\n",Math.round(D[l]*1000000.0)/1000000.0);
             }
             return;
        }
        //Base Case
        System.out.print("Base    : 0 :");
        for(int j = 0; j < n; j++) {
            System.out.printf(" P[" + j + "]=%.6f",Math.round(src_db[j]*1000000.0)/1000000.0);
        }

        if (iteration != 0) {
          for(int i = 0; i < iteration; i++)
          {
              for(int l = 0; l < n; l++) {
                D[l] = 0.0;
              }
            
              for(int j = 0; j < n; j++) {
                for(int k = 0; k < n; k++)
                {
                    if(Z[k][j] == 1) {
                        D[j] += src_db[k]/C[k];
                    } 
                }
              }


              System.out.println();
              System.out.print("Iter    : " + (i+1)+" : ");
              for(int l = 0; l < n; l++) {
                D[l] = d*D[l] + (1-d)/n;
                System.out.printf(" P[" + l + "]=%.6f",Math.round(D[l]*1000000.0)/1000000.0);
              }
            
              for(int l = 0; l < n; l++) {
                src_db[l] = D[l]; 
              } 
          }
          System.out.println();
        }
        else 
        {
          int i = 0;
          do {
               if(flag == true)
               {
                  flag = false;
               }
               else
               {
                 for(int l = 0; l < n; l++) {
                   src_db[l] = D[l];
                 }
               }
               for(int l = 0; l < n; l++) {
                 D[l] = 0.0;
               }

               for(int j = 0; j < n; j++) {
                 for(int k = 0; k < n; k++)
                 {
                    if(Z[k][j] == 1) {
                        D[j] += src_db[k]/C[k];
                    }
                 }
               }

               //Compute and print pagerank of all pages
               System.out.println(); 
               System.out.print("Iter    : " + (i+1)+" : ");
               for(int l = 0; l < n; l++) {
                 D[l] = d*D[l] + (1-d)/n;
                 System.out.printf(" P[" + l + "]=%.6f",Math.round(D[l]*1000000.0)/1000000.0);
               }
               i++;  
             } while (is_Converged(src_db, D) != true);  
        System.out.println(); 
        }
    }
}
