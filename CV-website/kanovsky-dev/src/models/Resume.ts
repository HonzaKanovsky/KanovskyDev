import { Experience } from "./Experience";
import { Education } from "./Education";
  export class Resume {
    title: string;
    experiences: Experience[];
    education: Education[];
  
    constructor(title: string, experiences: Experience[], education: Education[]) {
      this.title = title;
      this.experiences = experiences;
      this.education = education;
    }
  }
  