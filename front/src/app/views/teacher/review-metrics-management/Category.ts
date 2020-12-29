export interface Category {
    id: number;
    name: string;
    metrics: string;
    allMetrics: Assessment[];
  }
  export interface Assessment {
    id: number;
    description: string;
    link: string;
    mode: number;
    metrics: string;
    category: number;
  }
