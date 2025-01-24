// Resume.ts
export class Company {
    name: string;
    location: string;
  
    constructor(name: string, location: string) {
      this.name = name;
      this.location = location;
    }
  }
  
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
  
  // Education.ts
  export class University {
    name: string;
    location: string;
  
    constructor(name: string, location: string) {
      this.name = name;
      this.location = location;
    }
  }
  
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
  
  
  // Main Resume Class
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
  