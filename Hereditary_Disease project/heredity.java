import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class heredity
{   
    public static void main (String[] args){

  
    HashMap<String, Person> people = new HashMap<>();
    Person harry = new Person("Harry", "Lily", "James", null);
    Person lily = new Person("Lily", null, null, false);
    Person James = new Person("James", null, null, true);

    people.put("Harry", harry);
    people.put("Lily", lily);
    people.put("James", James);
    


    List<Person> People = new ArrayList<>(people.values());

        
    probabilities probabilities = new probabilities(People);
    
    Set<String> names = people.keySet();
    
    List<Set<String>> subset = generateSubsets(names);
    
  
    for (Set<String> have_traits : subset){
        Boolean fails_evidence = false;
        for (String person : names){
            if ((people.get(person).getTrait() != null) && (people.get(person).getTrait() != have_traits.contains(person))){
                fails_evidence = true;
                break;
            }
        }
        if (fails_evidence == true){
            continue;
        }
        
        for (Set<String> one_Gene : subset){
            Set<String> complementSet = new HashSet<>(names);
            complementSet.removeAll(one_Gene);
            List<Set<String>> complementPowerSet = generateSubsets(complementSet);
            for (Set<String> two_Genes : complementPowerSet) {
                
                
                double p = Joint_probability(people, one_Gene, two_Genes, have_traits);
                update(probabilities, one_Gene, two_Genes, have_traits, p);
            }
        }
    }

   normalize(probabilities);
    
    for (Person person : probabilities.people()) {
        System.out.println(person.getName() + ":");
        
        for (String field : probabilities.probabilities.get(person).keySet()) {
            System.out.println("  " + field.substring(0, 1).toUpperCase() + field.substring(1) + ":");
            
            for (Integer value : probabilities.probabilities.get(person).get(field).keySet()) {
                double p = probabilities.probabilities.get(person).get(field).get(value);
                System.out.printf("    %d: %.4f%n", value, p);
            }
        }
    }
    

    
    }
    public static List<Set<String>> generateSubsets(Set<String> set) {
        Set<Set<String>> uniqueSubsets = new HashSet<>();
        generateSubsetsHelper(set, new HashSet<>(), uniqueSubsets);
        return new ArrayList<>(uniqueSubsets);
    }

    private static void generateSubsetsHelper(Set<String> set, Set<String> currentSubset, Set<Set<String>> uniqueSubsets) {
        uniqueSubsets.add(new HashSet<>(currentSubset));

        for (String element : set) {
            if (!currentSubset.contains(element)) {
                currentSubset.add(element);
                generateSubsetsHelper(set, currentSubset, uniqueSubsets);
                currentSubset.remove(element);
            }
        }
    }
    public static double Joint_probability(HashMap<String, Person> people, Set<String> one_gene, Set<String> two_genes, Set<String> have_trait){
        double joint = 1;
        PROB probability = new PROB();

        // HashMap <String, HashMap<Integer, Double>> mother = new HashMap<>();
        // HashMap <String, HashMap<Integer, Double>> father = new HashMap<>(); 

        HashMap <Integer, Double> prob_gene_parent = new HashMap<>();
        prob_gene_parent.put(0,probability.get_mutation_probability());  
        prob_gene_parent.put(1, 0.5); 
        prob_gene_parent.put(2, 1 - probability.get_mutation_probability());

        for (String person : people.keySet()){ 
            double temp = 1;
            double father_inherit;
            double mother_inherit;
            String father = (String) people.get(person).getFather();
            String mother = (String) people.get(person).getMother();
            if (one_gene.contains(father)){
                father_inherit = prob_gene_parent.get(1);
            }
            else if (two_genes.contains(father)){
                father_inherit = prob_gene_parent.get(2);
            }
            else {
                father_inherit = prob_gene_parent.get(0); 
            }

            if (one_gene.contains(mother)){
                mother_inherit = prob_gene_parent.get(1);
            }
            else if (two_genes.contains(mother)){
                mother_inherit = prob_gene_parent.get(2);
            }
            else {
                mother_inherit = prob_gene_parent.get(0);
            }


            //probability of person having 0 copy of gene
            
            //if ((contains(one_gene, person) == false) && (contains(two_genes, person))){
            if (!one_gene.contains(person) && !two_genes.contains(person)){    
                //if we have no info about parent/no parent
                if (father == null && mother == null){
                    temp = temp * probability.get_genetic_probability(0);
                }
                //if we have info about parent
                else{
                    temp = (1 - father_inherit) * (1 - mother_inherit);
                }
            }

            //probability of person having 1 copy of gene
            if (one_gene.contains(person)){
                //if we have no info about parent
                if (father == null && mother == null){
                    temp = temp * probability.get_genetic_probability(1);
                }
                //if we have info about parent
                else {
                    temp = ((father_inherit) * (1 - mother_inherit)) + ((mother_inherit) * (1 - father_inherit));
                }

            }

            //probability of person having 2 copy of gene
            if (two_genes.contains(person)){
                //if no info about parent
                if (father == null && mother == null){
                    temp = temp * probability.get_genetic_probability(2);
                }
                else {
                    temp = father_inherit * mother_inherit;
                }
            }

            //if (contains(have_trait, person) == false){
            if (!have_trait.contains(person)){
                if (one_gene.contains(person)){
                    temp = temp * probability.get_trait_probability(1, false);
                }
                else if (two_genes.contains(person)){
                    temp = temp * probability.get_trait_probability(2, false);

                }
                else{
                    temp = temp * probability.get_trait_probability(0, false);
                }
            }  

            if (have_trait.contains(person)){
                if (one_gene.contains(person)){
                    temp = temp * probability.get_trait_probability(1, true);
                }
                else if (two_genes.contains(person)){
                    temp = temp * probability.get_trait_probability(2, true);
                }
                else{
                    temp = temp * probability.get_trait_probability(0, true);
                }
            }

            joint = joint * temp;


        }




        return joint;
    }

    public static void update (probabilities probability, Set<String> one_gene, Set<String> two_gene, Set<String> have_trait, double p){
        for (Person person : probability.people()){
            if (one_gene.contains(person.getName())){
                probability.update_gene_probability(person,1,p);
            }
            else if (two_gene.contains(person.getName())){
                probability.update_gene_probability(person, 2, p);
            }
            else {
                probability.update_gene_probability(person, 0, p);
            }

            if (have_trait.contains(person.getName())){
                probability.update_trait_probability(person, 1, p);
            }
            else {
                probability.update_trait_probability(person, 0, p);
            }
        }
    }


    public static void normalize (probabilities probability){
        for (Person person : probability.people()){
        
            double coefficient_trait = probability.sum_trait_probability(person);
            double normalized_no_trait = probability.get_trait_probability(person, 0) / coefficient_trait;
            double normalized_have_trait = probability.get_trait_probability(person, 1) / coefficient_trait;
            System.out.println(normalized_have_trait+normalized_no_trait);
            probability.normalize_trait(person, 0, normalized_no_trait);
            probability.normalize_trait(person, 1, normalized_have_trait);

            coefficient_trait = probability.sum_gene_probability(person);
            double normalized_zero_gene = probability.get_genetic_probability(person, 0) / coefficient_trait;
            double normalized_one_gene = probability.get_genetic_probability(person, 1) / coefficient_trait;
            double normalized_two_gene = probability.get_genetic_probability(person, 2) / coefficient_trait;
            System.out.println(normalized_one_gene+normalized_two_gene+normalized_zero_gene);

            probability.normalize_gene(person, 0, normalized_zero_gene);
            probability.normalize_gene(person, 1, normalized_one_gene);
            probability.normalize_gene(person, 2, normalized_two_gene);
        }
    }

  






    
}
class Person {
        private String name;
        private String mother;
        private String father;
        private Boolean trait;
    
        public Person(String name, String mother, String father, Boolean trait) {
            this.name = name;
            this.mother = mother;
            this.father = father;
            this.trait = trait;
        }
    
        public String getName() {
            return name;
        }
    
        public String getMother() {
            return mother;
        }
    
        public String getFather() {
            return father;
        }
    
        public Boolean getTrait() {
            return trait;
        }
    }