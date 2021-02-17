#include<iostream>
#include<string>
#include<vector>
#include<bitset>
#include<fstream>
#include <math.h>
using namespace std;
#define MemSize 1000 // memory size, in reality, the memory size should be 2^32, but for this lab, for the space reason, we keep it as this large number, but the memory is still 32-bit addressable.

struct IFStruct {
    bitset<32>  PC;
    bool        nop;  
};

struct IDStruct {
    bitset<32>  Instr;
    bool        nop;  
};

struct EXStruct {
    bitset<32>  Read_data1;
    bitset<32>  Read_data2;
    bitset<16>  Imm;
    bitset<5>   Rs;
    bitset<5>   Rt;
    bitset<5>   Wrt_reg_addr;
    bool        is_I_type;
    bool        rd_mem;//read memory?
    
    bool        wrt_mem; 
    bool        alu_op;     //1 for addu, lw, sw, 0 for subu 
    bool        wrt_enable;
    bool        nop;  
};

struct MEMStruct {
    bitset<32>  ALUresult;
    bitset<32>  Store_data;
    bitset<5>   Rs;
    bitset<5>   Rt;    
    bitset<5>   Wrt_reg_addr;
    bool        rd_mem;
    bool        wrt_mem; 
    bool        wrt_enable;    
    bool        nop;    
};

struct WBStruct {
    bitset<32>  Wrt_data;
    bitset<5>   Rs;
    bitset<5>   Rt;     
    bitset<5>   Wrt_reg_addr;
    bool        wrt_enable;
    bool        nop;     
};

struct stateStruct {
    IFStruct    IF;
    IDStruct    ID;
    EXStruct    EX;
    MEMStruct   MEM;
    WBStruct    WB;
};

class RF
{
    public: 
        bitset<32> Reg_data;
     	RF()
    	{ 
			Registers.resize(32);
            
			Registers[0] = bitset<32> (0);  
        }
	
        bitset<32> readRF(bitset<5> Reg_addr)
        {   
            Reg_data = Registers[Reg_addr.to_ulong()];
            
            return Reg_data;
        }
    
        void writeRF(bitset<5> Reg_addr, bitset<32> Wrt_reg_data)
        {
            Registers[Reg_addr.to_ulong()] = Wrt_reg_data;
        }
		 
		void outputRF()
		{
			ofstream rfout;
			rfout.open("RFresult.txt",std::ios_base::app);
			if (rfout.is_open())
			{
				rfout<<"State of RF:\t"<<endl;
				for (int j = 0; j<32; j++)
				{        
					rfout << Registers[j]<<endl;
				}
			}
			else cout<<"Unable to open file";
			rfout.close();               
		} 
			
	private:
		vector<bitset<32> >Registers;	
};

class INSMem
{
	public:
        bitset<32> Instruction;
    
        INSMem()
        {       
			IMem.resize(MemSize);
            
            ifstream imem;
            
			string line;
            
			int i=0;
            
			imem.open("imem.txt");
            
			if (imem.is_open())
                
			{
				while (getline(imem,line))
				{      
					IMem[i] = bitset<8>(line);
                    
					i++;
				}                    
			}
            else cout<<"Unable to open file";
            
			imem.close();                     
		}
                  
		bitset<32> readInstr(bitset<32> ReadAddress) 
		{    
			string insmem;
			insmem.append(IMem[ReadAddress.to_ulong()].to_string());
			insmem.append(IMem[ReadAddress.to_ulong()+1].to_string());
			insmem.append(IMem[ReadAddress.to_ulong()+2].to_string());
			insmem.append(IMem[ReadAddress.to_ulong()+3].to_string());
			Instruction = bitset<32>(insmem);		//read instruction memory
			return Instruction;     
		}     
      
    private:
    
        vector<bitset<8> > IMem;     
};
      
class DataMem    
{
    public:
    
        bitset<32> ReadData;
    
        DataMem() //initialize the data memory
        {
            DMem.resize(MemSize);
            
            ifstream dmem;
            
            string line;
            
            int i=0;
            
            dmem.open("dmem.txt");
            
            if (dmem.is_open())
            {
                
                while (getline(dmem,line))
                    
                {      
                    DMem[i] = bitset<8>(line);
                    
                    i++;
                    
                }
                
            }
            
            else cout<<"Unable to open file";
            
                dmem.close();          
        }
		
        bitset<32> readDataMem(bitset<32> Address)
        {
            
			string datamem;
            datamem.append(DMem[Address.to_ulong()].to_string());
            datamem.append(DMem[Address.to_ulong()+1].to_string());
            datamem.append(DMem[Address.to_ulong()+2].to_string());
            datamem.append(DMem[Address.to_ulong()+3].to_string());
            
            ReadData =
                 bitset<32>(datamem);		//read data memory
            
            return ReadData;
            
		}
            
        void writeDataMem(bitset<32> Address, bitset<32> WriteData)            
        {
            
            DMem[Address.to_ulong()] = bitset<8>(WriteData.to_string().substr(0,8));
            
            DMem[Address.to_ulong()+1] = bitset<8>(WriteData.to_string().substr(8,8));
            
            DMem[Address.to_ulong()+2] = bitset<8>(WriteData.to_string().substr(16,8));
            
            DMem[Address.to_ulong()+3] = bitset<8>(WriteData.to_string().substr(24,8));
            
        }   
                     
        void outputDataMem()
        {
            ofstream dmemout;
            
            dmemout.open("dmemresult.txt");
            
            if (dmemout.is_open())
                
            {
                
                for (int j = 0; j< 1000; j++)
                    
                {
                    
                    dmemout << DMem[j]<<endl;
                    
                }
                     
            }
            
            else cout<<"Unable to open file";
            
            dmemout.close();               
        }             
      
    private:
    
		vector<bitset<8> > DMem;
    
};  


void printState(stateStruct state, int cycle)
{
    ofstream printstate;
    
    printstate.open("stateresult.txt", std::ios_base::app);
    
    if (printstate.is_open())
        
    {
        printstate<<"State after executing cycle:\t"<<cycle<<endl;
        
        printstate<<"IF.PC:\t"<<state.IF.PC.to_ulong()<<endl;
        
        printstate<<"IF.nop:\t"<<state.IF.nop<<endl; 
        
        printstate<<"ID.Instr:\t"<<state.ID.Instr<<endl;
        
        printstate<<"ID.nop:\t"<<state.ID.nop<<endl;
        
        printstate<<"EX.Read_data1:\t"<<state.EX.Read_data1<<endl;
        printstate<<"EX.Read_data2:\t"<<state.EX.Read_data2<<endl;
        
        printstate<<"EX.Imm:\t"<<state.EX.Imm<<endl;
        
        printstate<<"EX.Rs:\t"<<state.EX.Rs<<endl;
        
        printstate<<"EX.Rt:\t"<<state.EX.Rt<<endl;
        printstate<<"EX.Wrt_reg_addr:\t"<<state.EX.Wrt_reg_addr<<endl;
        printstate<<"EX.is_I_type:\t"<<state.EX.is_I_type<<endl; 
        printstate<<"EX.rd_mem:\t"<<state.EX.rd_mem<<endl;
        printstate<<"EX.wrt_mem:\t"<<state.EX.wrt_mem<<endl;        
        printstate<<"EX.alu_op:\t"<<state.EX.alu_op<<endl;
        printstate<<"EX.wrt_enable:\t"<<state.EX.wrt_enable<<endl;
        printstate<<"EX.nop:\t"<<state.EX.nop<<endl;        

        printstate<<"MEM.ALUresult:\t"<<state.MEM.ALUresult<<endl;
        printstate<<"MEM.Store_data:\t"<<state.MEM.Store_data<<endl;
        
        printstate<<"MEM.Rs:\t"<<state.MEM.Rs<<endl;
        
        printstate<<"MEM.Rt:\t"<<state.MEM.Rt<<endl;   
        printstate<<"MEM.Wrt_reg_addr:\t"<<state.MEM.Wrt_reg_addr<<endl;              
        printstate<<"MEM.rd_mem:\t"<<state.MEM.rd_mem<<endl;
        printstate<<"MEM.wrt_mem:\t"<<state.MEM.wrt_mem<<endl; 
        printstate<<"MEM.wrt_enable:\t"<<state.MEM.wrt_enable<<endl;         
        printstate<<"MEM.nop:\t"<<state.MEM.nop<<endl;        

        printstate<<"WB.Wrt_data:\t"<<state.WB.Wrt_data<<endl;
        
        printstate<<"WB.Rs:\t"<<state.WB.Rs<<endl;
        
        printstate<<"WB.Rt:\t"<<state.WB.Rt<<endl;        
        printstate<<"WB.Wrt_reg_addr:\t"<<state.WB.Wrt_reg_addr<<endl;
        printstate<<"WB.wrt_enable:\t"<<state.WB.wrt_enable<<endl;
        
        printstate<<"WB.nop:\t"<<state.WB.nop<<endl;
        
    }
    
    else cout<<"Unable to open file";
    
    printstate.close();
    
}
 

int main()
{
    
    RF myRF;
    
    INSMem myInsMem;
    
    DataMem myDataMem;
    
    stateStruct state, newState;
    
    state.IF.nop = 0;
    
    state.ID.nop = 1;
    
    state.EX.nop = 1;
    
    state.MEM.nop = 1;
    
    state.WB.nop = 1;
    
    state.ID.Instr = 0;
    
    state.IF.PC = 0;
    
    bitset<6> opcode = 0;
    
    bitset<3> func = 0;
    
    
    bool stall = 0;
    
    bool branch1 = 0;
    
    int cycle = 0;
			
             
    while (1) {
        
        

        /* --------------------- WB stage --------------------- */
        if(!state.WB.nop){
            
            if(state.WB.wrt_enable){
                //sw, addu, subu
                
                myRF.writeRF(state.WB.Wrt_reg_addr, state.WB.Wrt_data);
                
            }
            
            
        }



        /* --------------------- MEM stage --------------------- */
        if(!state.MEM.nop){
            
            newState.WB.nop = 0;
            
            if(state.MEM.wrt_mem){
                //sw
                if(state.WB.Wrt_reg_addr == state.MEM.Rt){
                    
                    state.MEM.Store_data = state.WB.Wrt_data;
                    
                }//judge mem-mem forwarding
                myDataMem.writeDataMem(state.MEM.ALUresult, state.MEM.Store_data);
                
                newState.WB.wrt_enable = 0;
                
            }
            
             if(state.MEM.wrt_enable)
           //addu, subu, lw
             {
               // I and R both have Rt and Rs
                 newState.WB.Rs = state.MEM.Rs;
                
                newState.WB.Rt = state.MEM.Rt;
                 newState.WB.wrt_enable = 1;
                 
                 newState.WB.Wrt_reg_addr = state.MEM.Wrt_reg_addr;
                 
                 newState.WB.Wrt_data = state.MEM.ALUresult;//R
                 
                 if(state.MEM.rd_mem){
                //lw
                
                
                     newState.WB.Wrt_data = myDataMem.readDataMem(state.MEM.ALUresult);
                
                   }
                 
        }
            
            
        }
        
      


        /* --------------------- EX stage --------------------- */
        if(!state.EX.nop){
            
            newState.MEM.nop = 0;
            
            newState.MEM.Rs = state.EX.Rs;
            
            newState.MEM.Rt = state.EX.Rt;
        
            
                      if(state.EX.is_I_type){//I-type
                        
                
            if(state.EX.wrt_mem){//sw
                
                newState.MEM.Store_data = state.EX.Read_data2;
                
                if(state.WB.wrt_enable){
                    
                    if(state.EX.Rt == state.WB.Wrt_reg_addr){
                        
                        newState.MEM.Store_data = state.WB.Wrt_data;
                        
                    }
                    
                }//add+bne(not taken)+ sw hazard
                
                
                newState.MEM.wrt_mem = 1;
                
                
                newState.MEM.rd_mem = 0;
                
                newState.MEM.wrt_enable = 0;
                
                
                
                
                newState.MEM.ALUresult = - pow(2,32)*state.EX.Read_data1[31] + state.EX.Read_data1.to_ulong() - pow(2,16)*state.EX.Imm[15] + state.EX.Imm.to_ulong();
                
            }
                          
                          if(state.EX.rd_mem){
                     //lw
                              newState.MEM.Wrt_reg_addr = state.EX.Wrt_reg_addr;
                              
                              newState.MEM.rd_mem = 1;
                              
                              newState.MEM.wrt_enable = 1;
                              
                              newState.MEM.wrt_mem = 0;
                              
                               if(newState.WB.wrt_enable)
                               {
                                   
                                   if(newState.WB.Wrt_reg_addr == state.EX.Rs){
                                       
                                       state.EX.Read_data1 = newState.WB.Wrt_data;
                                       
                                   }
                                   
                                   
                               }//lw forwarding judge
                          
                              newState.MEM.ALUresult = - pow(2,32)*state.EX.Read_data1[31] + state.EX.Read_data1.to_ulong() - pow(2,16)*state.EX.Imm[15] + state.EX.Imm.to_ulong();
                              
                          }
                          
            
                          
                
                
            
                      }
            else{//R-type
                
                
                
                newState.MEM.Wrt_reg_addr = state.EX.Wrt_reg_addr;
                
                newState.MEM.rd_mem = 0;
                         
                newState.MEM.wrt_mem = 0;
                         
                newState.MEM.wrt_enable = 1;
                
                    
                        if(state.MEM.wrt_enable){
                            //forwarding judge
                            if(state.MEM.Wrt_reg_addr == state.EX.Rs){
                                
                                state.EX.Read_data1 = state.MEM.ALUresult;
                                
                            }
                            
                            if(state.MEM.Wrt_reg_addr == state.EX.Rt){
                                
                                state.EX.Read_data2 = state.MEM.ALUresult;
                                
                            }
                            
                        }
                        if(state.WB.wrt_enable)//make sure do not get value if have gotten value from previous ex stage
                        {
                            
                            if(state.WB.Wrt_reg_addr == state.EX.Rs){
                                
                                state.EX.Read_data1 = state.WB.Wrt_data;
                                
                            }
                            
                            if(state.WB.Wrt_reg_addr == state.EX.Rt){
                                
                                state.EX.Read_data2 = state.WB.Wrt_data;
                                
                            }
                            
                            
                        }//forwarding judge
                
                
                if(state.EX.alu_op) {
                    newState.MEM.ALUresult = - pow(2,32)*state.EX.Read_data1[31] + state.EX.Read_data1.to_ulong() - pow(2,32)*state.EX.Read_data2[31] + state.EX.Read_data2.to_ulong();//signed
                    
                    
                    
                }
                else{
                    
                    newState.MEM.ALUresult = - pow(2,32)*state.EX.Read_data1[31] + state.EX.Read_data1.to_ulong() + pow(2,32)*state.EX.Read_data2[31] - state.EX.Read_data2.to_ulong();//signed
                    
                }
                
            }
            
        }
            
        
     
          

        /* --------------------- ID stage --------------------- */
        if(!state.ID.nop){
            
            newState.EX.nop = 0;
            
            opcode = (state.ID.Instr.to_ulong() & 0xFC000000) >>26;
            
            switch (opcode.to_ulong()) {
                    
                case 0://R-type
                {
                    
        newState.EX.is_I_type = 0;
                    
           newState.EX.rd_mem = 0;
                    
           newState.EX.wrt_mem = 0;
                    
           newState.EX.wrt_enable = 1;
                    
        func = state.ID.Instr.to_ulong() & 0x7;
                    
                    if(func.to_ulong() == 1){
                   // modify:ADDU func = 1
                        newState.EX.alu_op = 1;
                        
                    }
                    else{
                        
                newState.EX.alu_op = 0;
                        
                    }

            newState.EX.Rs = (state.ID.Instr.to_ulong() & 0x03E00000)>>21;

            newState.EX.Rt = (state.ID.Instr.to_ulong() & 0x001F0000)>>16;

            newState.EX.Wrt_reg_addr = (state.ID.Instr.to_ulong() & 0x0000F800)>>11;
                    
                    
        newState.EX.Read_data1 =  myRF.readRF(newState.EX.Rs);
                    
        newState.EX.Read_data2 =  myRF.readRF(newState.EX.Rt);
              
                   
                        if(state.EX.rd_mem){
                                               if((state.EX.Wrt_reg_addr == newState.EX.Rs)||(state.EX.Wrt_reg_addr == newState.EX.Rt)){
                                                   
                                                   
                                                   stall = 1;
                                                   
                                                   
                                               }// judge stall
                        
                    }
                    
                    
                    break;
                    
                }
                    
                case 35://lw
                {
                  newState.EX.Rs = (state.ID.Instr.to_ulong() & 0x03E00000)>>21;

            newState.EX.Rt = (state.ID.Instr.to_ulong() & 0x001F0000)>>16;
                    
        newState.EX.Read_data1 =  myRF.readRF(newState.EX.Rs);
                    
        newState.EX.Wrt_reg_addr = newState.EX.Rt;
                

            newState.EX.Imm = state.ID.Instr.to_ulong() & 0x0000FFFF;//signed
                    
            newState.EX.is_I_type = 1;
                             
            
                             
            newState.EX.wrt_mem = 0;
                             
            newState.EX.wrt_enable = 1;
                    
            newState.EX.alu_op = 1;
                    
            newState.EX.rd_mem = 1;
                    
                
             
                
                    
                    break;
                        
                }
                    
                case 43://sw
                {
                  
            newState.EX.Rs = (state.ID.Instr.to_ulong() & 0x03E00000)>>21;

            newState.EX.Rt = (state.ID.Instr.to_ulong() & 0x001F0000)>>16;

            newState.EX.Imm = state.ID.Instr.to_ulong() & 0x0000FFFF;//signed
                    
                newState.EX.Read_data1 =  myRF.readRF(newState.EX.Rs);
                            
                newState.EX.Read_data2 =  myRF.readRF(newState.EX.Rt);
                    
                    
                    newState.EX.is_I_type = 1;
                                     
                    newState.EX.rd_mem = 0;
                                     
                    newState.EX.wrt_mem = 1;
                                     
                    newState.EX.wrt_enable = 0;
                            
                    newState.EX.alu_op = 1;
                    
                    break;
                    
                }
                
                case 4://beq
                {
                    
               newState.EX.Rs = (state.ID.Instr.to_ulong() & 0x03E00000)>>21;

                    newState.EX.Rt = (state.ID.Instr.to_ulong() & 0x001F0000)>>16;
                    
                   
                    
                  bitset<18> imm = (state.ID.Instr.to_ulong() & 0x0000FFFF)<<2;
                            
 
                            
                            
                            newState.EX.is_I_type = 1;
                                             
                            newState.EX.rd_mem = 0;
                                             
                            newState.EX.wrt_mem = 0;
                                             
                            newState.EX.wrt_enable = 0;
                                    
                            newState.EX.alu_op = 0;
                    
                    if(myRF.readRF(newState.EX.Rs) != myRF.readRF(newState.EX.Rt)){
                        
                        branch1 = 1;
                        
                        newState.IF.PC = state.IF.PC.to_ulong() - pow(2,18) * imm[17] + imm.to_ulong();//bne signed
                        
                        newState.ID.nop = 1;
                        
                        newState.EX.nop = 1;
                        
                        
                        
                    }
                    else{
                        
                        newState.EX.nop = 1;
                        
                    }//beq judge
                       
                       
                       
                    break;
                    
                }
                
               default:
                    ;
                    
            }
            
            
            
            
                    
        }
                
            
    /* --------------------- IF stage --------------------- */
   
        
        
if(!stall){
        if(!state.IF.nop){
                
                
                    newState.ID.Instr = myInsMem.readInstr(state.IF.PC);
                
            if(!branch1){//beq-halt hazard
            
                if(newState.ID.Instr.to_ulong() == 0xFFFFFFFF)//halt
                {
                    
                    newState.IF.nop = 1;
                    
                    newState.ID.nop = 1;
                    
                    
                }
                else{
                    
               newState.IF.PC = state.IF.PC.to_ulong() + 4;
                
                    newState.ID.nop = 0;
                        
                }
                
            }
                
            }
        }
    
        
    
     
   
        
    if(state.ID.nop) {
        
        newState.EX.nop = 1;
        
    }

    if(state.EX.nop){
        
        newState.MEM.nop = 1;
        
    }

    if(state.MEM.nop){
        
        newState.WB.nop = 1;
        
    }
        
        if(stall){
            
            newState.ID = state.ID;
            
            newState.IF = state.IF;
            
            newState.EX.nop = 1;
            
            stall = 0;
            
        }
        
        if(branch1){
            
            branch1 = 0;
            
        }

 

                 
            if (state.IF.nop && state.ID.nop && state.EX.nop && state.MEM.nop && state.WB.nop)
                break;
            
            printState(newState, cycle); //print states after executing cycle 0, cycle 1, cycle 2 ...
           
            state = newState; /*The end of the cycle and updates the current state with the values calculated in this cycle */
                    
        
        myRF.outputRF(); // dump RF;
        myDataMem.outputDataMem(); // dump data mem
        
        cycle++;
    
    }
    
    return 0;
            
        }

        
        

