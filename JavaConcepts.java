import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Hafsah Shehzad
 */
public class Assignment{

	public static abstract class Phone{
		int ModelNo;
		boolean hasKeypad;
		boolean hasCallerID;
                
		
		Phone(int model, boolean keys, boolean ID){
                    this.ModelNo=model;
                    this.hasKeypad=keys;
                    this.hasCallerID=ID;
                }
                
                public final void Dials(){
                    System.out.println("Can make outgoing calls");
                }
                public final void Calls(){
                    System.out.println("Can take ingoing calls");
                }
                public abstract void CallerID();
		public abstract void printspecs();
		
	}
        
        public interface OS{
            
            public void OSVersion();
            public void CodeName();
            
           
            
        }
        
        public interface RAM{
            public void defineRAM();
        }
        
       
        
        public static class Smartphone extends Phone implements OS , RAM{
            
             public class Processor{
                String Core;
                String Company;
                boolean GPU;
                
                Processor(String Core,String Company, boolean GPU){
                    this.Core=Core;
                    this.Company=Company;
                    this.GPU=GPU;
                }
                
                public void HasGPU(){
                    try{
                    if(GPU==true){
                        System.out.println("Built in GPU");
                    }
                    else{
                        System.out.println("No internal GPU");
                    }
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                public void print(){
                    System.out.println("Manufacturer: "+Company);
                    System.out.println("Core: "+Core);
                    HasGPU();
                }
                
            }
            
            Processor processor;
            int CameraQ;
            int mem;
            String Version;
            String CodeName;
            
            public void defineRAM(){
                System.out.println("Defining RAM: "+mem);
            }
            
            public void OSVersion(){
                System.out.println("OS Version: "+ Version);
            }
            public void CodeName(){
                System.out.println("OS name: "+CodeName);
            }
            
            Smartphone(int Model, boolean keys, boolean ID, int CameraQ, String company, String Core, boolean GPU, int mem, String Version, String CodeName){
                super(Model,keys,ID);
                this.CameraQ=CameraQ;
                this.processor=new Processor(Core,company,GPU);
                this.Version=Version;
                this.CodeName=CodeName;
                this.mem=mem;
            }
            
            public void CallerID(){
                System.out.println("Caller ID stored in cloud or logbook");
            }
                
            public void printspecs(){
                 System.out.println("SmartPhone specs");
                 System.out.println("Camera quality is "+CameraQ+" mp");
                 processor.HasGPU();
                 defineRAM();
            }
        }
        
        public static class FeaturePhone extends Phone implements RAM{
            public static class Battery{
                int duration;
                int life;
                
                Battery(int duration, int life){
                    this.duration=duration;
                    this.life=life;
                }
                
                public void printBattery(){
                    System.out.println("battery specs");
                    System.out.println("Life: "+life+"years");
                    System.out.println("Duration: "+duration+"hours");
                }
                
            }
            boolean hasEntertainment;
            int memory;
            boolean SSD;
            
            
            
            
            FeaturePhone(int model, boolean keys, boolean ID, boolean ent, int mem, boolean SSD){
                super(model,keys,ID);
                this.hasEntertainment=ent;
                this.SSD=SSD;
                this.memory=mem;
            }
            public void defineRAM(){
                System.out.println("Defining RAM: "+memory);
            }
            
            public void CallerID(){
                System.out.println("Caller ID stored in sim");
            }
            public void hasSSD(){
                try{
                if(SSD==true){
                    System.out.println("SSD slot available");
                    
                }
                else{
                    
                    System.out.println("No SSD slot");
                }
                }catch(Exception e){
                 System.out.println(e.getMessage());
                        
                }
                
            }
                
            
            public void Entertainment(){
                if(hasEntertainment==true){
                    System.out.println("Contains app store, GPS and games");
                }
                else{
                    System.out.println("No games and GPS");
                }
            }
            public void printspecs(){
                System.out.println("Feature phone");
                System.out.println("Memory available: "+memory);
                Entertainment();
                hasSSD();
                defineRAM();
                
                
            }
        }
        
        public static class Android extends Smartphone{
            
            int API;
            ArrayList<String> companies;
            
            Android(int Model, boolean keys, boolean ID, int CameraQ, String company, String Core, boolean GPU, int mem,String Version, String CodeName, int API){
                super(Model,keys,ID,CameraQ,company,Core,GPU,mem,Version,CodeName);
                this.API=API;
                companies=new ArrayList<String>();
            }
            
            public void addCompany(String name){
                try{
                companies.add(name);
                }catch(ArrayIndexOutOfBoundsException e ){
                    System.out.println(e.getMessage());
                }catch(NullPointerException e){
                    System.out.println(e.getMessage());
                }
            }
            
            public void printAndroidversion(){
                System.out.println("Android details");
                System.out.println("API: "+API);
                this.OSVersion();
                this.CodeName();
            }
            
            
        }
        
        public static class IOS extends Smartphone{
            
            final String company="iPhone";
            
            
            IOS(int Model, boolean keys, boolean ID, int CameraQ, String company, String Core, boolean GPU, int mem,String Version, String CodeName){
                super(Model,keys,ID,CameraQ,company,Core,GPU,mem,Version,CodeName);
            }
            
            public void printIOS(){
                System.out.println("IOS details");
                this.OSVersion();
                this.CodeName();
            }
        }
        
        public static void main(String[] args) throws IOException{
            
            Android A1=new Android(123,true,true,10,"SnapDragon","Dual",true,16,"9.0","Pie",11);
            A1.addCompany("Samsung");
            A1.printAndroidversion();
            A1.printspecs();
            A1.CallerID();
            A1.Dials();
            A1.Dials();
            
             System.out.println("--------");
            IOS I1=new IOS(145, true,true,12,"SnapDragon","Quad",true,32,"8.4","IOS");
            I1.printIOS();
            I1.printspecs();
            I1.CallerID();
            I1.Dials();
            
            System.out.println("--------");
            FeaturePhone f1=new FeaturePhone(88876,true,true, false, 4, true);
            f1.defineRAM();
            f1.printspecs();
            f1.Entertainment();
            f1.CallerID();
            
             System.out.println("--------");
            Smartphone s1=new Smartphone(2345,true,true,8,"Quadra","Dual",true,12,"1.2","Lollipop");
            Smartphone.Processor p1= s1.new Processor("Quadra","Single",true);
            p1.print();
            
            
            
        }