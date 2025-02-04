 import { Company } from "./Company";
 
 export class Experience {
    position: string;
    period: string;
    description: string;
    company: Company;
  
    constructor(position: string, period: string, description: string, company: Company) {
      this.position = position;
      this.period = period;
      this.description = description;
      this.company = company;
    }
  }