//Name: Rohit Thole
//UCID: 31509340

import java.util.*;
import java.io.*;
import java.lang.*;
import static java.lang.Math.*;

public class hits9340 {

        int iteration;
        int initial_val;
        String filename;
        int n;      // number of vertices in the graph
        int m;      // number of edges in the graph
        int[][] Z;  // adjacency matrix 
        double[] temp1;
        double[] temp2;
        final double errorrate = 0.00001; 

    hits9340() {} //default constructor

    hits9340(int iteration, int initial_val, String filename)   
    {
        this.iteration = iteration;
        this.initial_val = initial_val;
        this.filename = filename;
        try {        
            Scanner scanner = new Scanner(new File(filename));
            n = scanner.nextInt();
            m = scanner.nextInt();
            
            Z = new int[n][n];
            for(int i = 0; i < n; i++)
             for(int j = 0; j < n; j++)
               Z[i][j] = 0;

            while(scanner.hasNextInt())
            {
                Z[scanner.nextInt()][scanner.nextInt()] = 1; 
                //System.out.println(scanner.nextInt());
            }
            

            temp1 = new double[n];
            temp2 = new double[n];
            switch(initial_val) {
            case 0:
              for(int i = 0; i < n; i++) {
                temp1[i] = 0;
                temp2[i] = 0;
              }
              break;
            case 1:
              for(int i = 0; i < n; i++) {
                temp1[i] = 1;
                temp2[i] = 1;
              }
              break;
            case -1:
              for(int i =0; i < n; i++) {
                temp1[i] = 1.0/n;
                temp2[i] = 1.0/n;
              }
              break;
            case -2:
              for(int i =0; i < n; i++) {
                temp1[i] = 1.0/Math.sqrt(n);
                temp2[i] = 1.0/Math.sqrt(n);
              }
              break;
            }

        }
        catch(FileNotFoundException fnfe)
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
            System.out.println("Enter -2, -1, 0 or 1 for initialvalue");
            return;
        }

        hits9340 ht = new hits9340(iterations, initialvalue, filename);

        ht.hitsAlgo();
    }
 
    boolean isConverged(double[] p, double[] q)
    {
       for(int i = 0 ; i < n; i++) {
           if ( abs(p[i] - q[i]) > errorrate ) 
             return false;
       }
       return true;
    } 
    
    public void hitsAlgo()
    {
        double[] h = new double[n];
        double[] a = new double[n];
        double temp_scale_factor = 0.0;
        double temp_sum_square = 0.0;
        double h_scale_factor = 0.0;
        double h_sum_square = 0.0; 
        double[] aprev = new double[n]; 
        double[] hprev = new double[n]; 

        //If the graph has N greater than 10, then the values for iterations, initialvalue revert to 0 and -1 respectively
        if(n > 10) {
            iteration = 0;
            for(int i =0; i < n; i++) {
                h[i] = 1.0/n;
                a[i] = 1.0/n;
                hprev[i] = h[i];
                aprev[i] = a[i];
            }
            
          int i = 0;
          do {  
               for(int r = 0; r < n; r++) {
                   aprev[r] = a[r];
                   hprev[r] = h[r];
               }

                //A step starts
                for(int p = 0; p < n; p++) {
                    a[p] = 0.0;
                }
            
                for(int j = 0; j < n; j++) {
                    for(int k = 0; k < n; k++) {
                        if(Z[k][j] == 1) {
                            a[j] += h[k]; 
                        }
                    }
                }//A step ends

                //H step starts
                for(int p = 0; p < n; p++) {
                    h[p] = 0.0;
                }

                for(int j = 0; j < n; j++) {
                    for(int k = 0; k < n; k++) {
                        if(Z[j][k] == 1) {
                            h[j] += a[k]; 
                        }
                    }
                }//H step ends

                //Scaling A starts
                temp_scale_factor = 0.0;
                temp_sum_square = 0.0;
                for(int l = 0; l < n; l++) {
                    temp_sum_square += a[l]*a[l];    
                }
                temp_scale_factor = Math.sqrt(temp_sum_square); 
                for(int l = 0; l < n; l++) {
                    a[l] = a[l]/temp_scale_factor;
                } 
 
               
                h_scale_factor = 0.0;
                h_sum_square = 0.0;
                for(int l = 0; l < n; l++) {
                    h_sum_square += h[l]*h[l];    
                }
                h_scale_factor = Math.sqrt(h_sum_square); 
                for(int l = 0; l < n; l++) {
                    h[l] = h[l]/h_scale_factor;
                }
                i++; 
          } while( false == isConverged(a, aprev) || false == isConverged(h, hprev));
          System.out.println("Iter:    " + i);
          for(int l = 0; l < n; l++) {
              System.out.printf(" A/H[%d]=%.6f/%.6f\n",l,Math.round(a[l]*1000000.0)/1000000.0,Math.round(h[l]*1000000.0)/1000000.0); 
          }
          return;
        }

        for(int i = 0; i < n; i++)
        {
            h[i] = temp1[i];
            a[i] = temp2[i];
            hprev[i] = h[i];
            aprev[i] = a[i]; 
        }
        
        System.out.print("Base:    0 :");
        for(int i = 0; i < n; i++) {
          System.out.printf(" A/H[%d]=%.6f/%.6f",i,Math.round(temp2[i]*1000000.0)/1000000.0,Math.round(temp1[i]*1000000.0)/1000000.0); 
          //System.out.println("temp2[" + i + "]= " + temp2[i]); 
        }
        
        if (iteration != 0) { 
            for(int i = 0; i < iteration; i++) { //iteration starts
            
                //A step starts
                for(int p = 0; p < n; p++) {
                    a[p] = 0.0;
                }
            
                for(int j = 0; j < n; j++) {
                    for(int k = 0; k < n; k++) {
                        if(Z[k][j] == 1) {
                            a[j] += h[k]; 
                        }
                    }
                }

                
                for(int p = 0; p < n; p++) {
                    h[p] = 0.0;
                }

                for(int j = 0; j < n; j++) {
                    for(int k = 0; k < n; k++) {
                        if(Z[j][k] == 1) {
                            h[j] += a[k]; 
                        }
                    }
                }

              
                temp_scale_factor = 0.0;
                temp_sum_square = 0.0;
                for(int l = 0; l < n; l++) {
                    temp_sum_square += a[l]*a[l];    
                }
                temp_scale_factor = Math.sqrt(temp_sum_square); 
                for(int l = 0; l < n; l++) {
                    a[l] = a[l]/temp_scale_factor;
                } 
 
                
                h_scale_factor = 0.0;
                h_sum_square = 0.0;
                for(int l = 0; l < n; l++) {
                    h_sum_square += h[l]*h[l];    
                }
                h_scale_factor = Math.sqrt(h_sum_square); 
                for(int l = 0; l < n; l++) {
                    h[l] = h[l]/h_scale_factor;
                }
            
                System.out.println();
                System.out.print("Iter:    " + (i+1) + " :");
                for(int l = 0; l < n; l++) {
                    System.out.printf(" A/H[%d]=%.6f/%.6f",l,Math.round(a[l]*1000000.0)/1000000.0,Math.round(h[l]*1000000.0)/1000000.0); 
                }
   
            }
        } 
        else
        {
          int i = 0;
          do {  
                for(int r = 0; r < n; r++) {
                    aprev[r] = a[r];
                    hprev[r] = h[r];
                }

                //A step starts
                for(int p = 0; p < n; p++) {
                    a[p] = 0.0;
                }
            
                for(int j = 0; j < n; j++) {
                    for(int k = 0; k < n; k++) {
                        if(Z[k][j] == 1) {
                            a[j] += h[k]; 
                        }
                    }
                }

                
                for(int p = 0; p < n; p++) {
                    h[p] = 0.0;
                }

                for(int j = 0; j < n; j++) {
                    for(int k = 0; k < n; k++) {
                        if(Z[j][k] == 1) {
                            h[j] += a[k]; 
                        }
                    }
                }

                
                temp_scale_factor = 0.0;
                temp_sum_square = 0.0;
                for(int l = 0; l < n; l++) {
                    temp_sum_square += a[l]*a[l];    
                }
                temp_scale_factor = Math.sqrt(temp_sum_square); 
                for(int l = 0; l < n; l++) {
                    a[l] = a[l]/temp_scale_factor;
                }
 
                //Scaling H starts
                h_scale_factor = 0.0;
                h_sum_square = 0.0;
                for(int l = 0; l < n; l++) {
                    h_sum_square += h[l]*h[l];    
                }
                h_scale_factor = Math.sqrt(h_sum_square); 
                for(int l = 0; l < n; l++) {
                    h[l] = h[l]/h_scale_factor;
                }// Scaling H ends
                i++; // incr the interation counter
                System.out.println();
                System.out.print("Iter:    " + i + " :");
                for(int l = 0; l < n; l++) {
                    System.out.printf(" A/H[%d]=%.6f/%.6f",l,Math.round(a[l]*1000000.0)/1000000.0,Math.round(h[l]*1000000.0)/1000000.0); 
                }
          } while( false == isConverged(a, aprev) || false == isConverged(h, hprev));
        }
        System.out.println();
    }
}
