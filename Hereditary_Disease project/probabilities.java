import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class probabilities {
    public HashMap<Person, HashMap<String, HashMap<Integer,BigDecimal>>> probabilities = new HashMap<>();

    public probabilities(List<Person> people){
        
        for (Person person: people){
            HashMap<String, HashMap<Integer,BigDecimal>> personprobability = new HashMap<>();
            HashMap<Integer,BigDecimal> geneprobability = new HashMap<>();
            geneprobability.put(2, new BigDecimal(0.0));
            geneprobability.put(1, new BigDecimal(0.0));
            geneprobability.put(0, new BigDecimal(0.0));

            HashMap<Integer,BigDecimal> traitprobability = new HashMap<>();
            traitprobability.put(1, new BigDecimal(0.0));
            traitprobability.put(0, new BigDecimal(0.0));

            personprobability.put("gene", geneprobability);
            personprobability.put("trait", traitprobability);
            this.probabilities.put(person, personprobability);
        }
    }

    public BigDecimal get_genetic_probability (Person name, int gene_number){
        return probabilities.get(name).get("gene").get(gene_number);
    }

    public BigDecimal get_trait_probability (Person name, int trait_number){
        return probabilities.get(name).get("trait").get(trait_number);
    }



    public void update_gene_probability(Person name, int gene_number, BigDecimal new_probability) {
        HashMap<String, HashMap<Integer, BigDecimal>> personProbability = this.probabilities.get(name);
       
        
        BigDecimal previousValue = personProbability.get("gene").get(gene_number);
        BigDecimal newValue = previousValue.add(new_probability);
    
        // Update the existing gene probability for the specified gene_number
        personProbability.get("gene").put(gene_number, newValue);
    
        // No need to create a new HashMap for "gene"; just update the existing one
        this.probabilities.put(name, personProbability);
    }
    
    public void update_trait_probability(Person name, int trait_number, BigDecimal new_probability) {
        HashMap<String, HashMap<Integer, BigDecimal>> personProbability = this.probabilities.get(name);
    
        BigDecimal previousValue = personProbability.get("trait").get(trait_number);
        BigDecimal newValue = previousValue.add(new_probability);
    
        // Update the existing trait probability for the specified trait_number
        personProbability.get("trait").put(trait_number, newValue);
    
        // No need to create a new HashMap for "trait"; just update the existing one
        this.probabilities.put(name, personProbability);
    }





    public Set<Person> people (){
        return probabilities.keySet();
    }

    public BigDecimal sum_trait_probability(Person person){
        BigDecimal no_trait_probability = probabilities.get(person).get("trait").get(0);
        BigDecimal have_trait_probability = probabilities.get(person).get("trait").get(1);
        return no_trait_probability.add(have_trait_probability);
    }

    public BigDecimal sum_gene_probability(Person person){
        BigDecimal zero_gene_probability = probabilities.get(person).get("gene").get(0);
        BigDecimal one_gene_probability = probabilities.get(person).get("gene").get(1);
        BigDecimal two_gene_probability = probabilities.get(person).get("gene").get(2);

        BigDecimal oneplustwo = one_gene_probability.add(two_gene_probability);
        return oneplustwo.add(zero_gene_probability);
    }

    public void normalize_gene (Person person, int gene_number, BigDecimal normalized_probability){
        HashMap<String, HashMap<Integer, BigDecimal>> personProbability = this.probabilities.get(person);
    
        
    
        
        personProbability.get("gene").put(gene_number, normalized_probability);
    
        // No need to create a new HashMap for "trait"; just update the existing one
        this.probabilities.put(person, personProbability);
    }

    public void normalize_trait (Person person, int trait_number, BigDecimal normalized_probability){
        HashMap<String, HashMap<Integer, BigDecimal>> personProbability = this.probabilities.get(person);
    
        personProbability.get("trait").put(trait_number, normalized_probability);
    
        // No need to create a new HashMap for "trait"; just update the existing one
        this.probabilities.put(person, personProbability);
    }
}