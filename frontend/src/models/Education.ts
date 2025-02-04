  import { University } from "./University";
  
  export class Education {
    degree: string;
    period: string;
    description: string;
    university: University;
  
    constructor(degree: string, period: string, description: string, university: University) {
      this.degree = degree;
      this.period = period;
      this.description = description;
      this.university = university;

    }
  }