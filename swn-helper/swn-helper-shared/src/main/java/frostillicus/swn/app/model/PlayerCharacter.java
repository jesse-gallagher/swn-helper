package frostillicus.swn.app.model;

import java.util.List;

import org.jnosql.artemis.Column;
import org.jnosql.artemis.Embeddable;
import org.jnosql.artemis.Entity;
import org.jnosql.artemis.Id;

@Entity
public class PlayerCharacter {
	@Embeddable
	public static class Stats {
		@Column private int strength;
		@Column private int intelligence;
		@Column private int dexterity;
		@Column private int wisdom;
		@Column private int constitution;
		@Column private int charisma;
		
		public int getStrength() {
			return strength;
		}
		public void setStrength(int strength) {
			this.strength = strength;
		}
		public int getIntelligence() {
			return intelligence;
		}
		public void setIntelligence(int intelligence) {
			this.intelligence = intelligence;
		}
		public int getDexterity() {
			return dexterity;
		}
		public void setDexterity(int dexterity) {
			this.dexterity = dexterity;
		}
		public int getWisdom() {
			return wisdom;
		}
		public void setWisdom(int wisdom) {
			this.wisdom = wisdom;
		}
		public int getConstitution() {
			return constitution;
		}
		public void setConstitution(int constitution) {
			this.constitution = constitution;
		}
		public int getCharisma() {
			return charisma;
		}
		public void setCharisma(int charisma) {
			this.charisma = charisma;
		}
		
		@SuppressWarnings("nls")
		@Override
		public String toString() {
			return "Stats [strength=" + strength + ", intelligence=" + intelligence + ", dexterity=" + dexterity + ", wisdom=" + wisdom + ", constitution="
					+ constitution + ", charisma=" + charisma + "]";
		}
	}
	@Embeddable
	public static class Saves {
		@Column private int physicalEffect;
		@Column private int mentalEffect;
		@Column private int evasion;
		@Column private int technology;
		@Column private int luck;

		public int getPhysicalEffect() {
			return physicalEffect;
		}
		public void setPhysicalEffect(int physicalEffect) {
			this.physicalEffect = physicalEffect;
		}
		public int getMentalEffect() {
			return mentalEffect;
		}
		public void setMentalEffect(int mentalEffect) {
			this.mentalEffect = mentalEffect;
		}
		public int getEvasion() {
			return evasion;
		}
		public void setEvasion(int evasion) {
			this.evasion = evasion;
		}
		public int getTechnology() {
			return technology;
		}
		public void setTechnology(int technology) {
			this.technology = technology;
		}
		public int getLuck() {
			return luck;
		}
		public void setLuck(int luck) {
			this.luck = luck;
		}
		@Override
		public String toString() {
			return String.format("Saves [physicalEffect=%s, mentalEffect=%s, evasion=%s, technology=%s, luck=%s]", physicalEffect, mentalEffect, evasion, technology, luck); //$NON-NLS-1$ 
		}
	}
	@Embeddable
	public static class Skill {
		@Column private String name;
		@Column private int level;
		
		public Skill() { }
		public Skill(String name, int level) {
			this.name = name;
			this.level = level;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getLevel() {
			return level;
		}
		public void setLevel(int level) {
			this.level = level;
		}
		
		@Override
		public String toString() {
			return String.format("Skill [name=%s, level=%s]", name, level); //$NON-NLS-1$
		}
	}
	
	@Id private String id;
	
	@Column private String playerId;
	
	@Column private String name;
	@Column private String className;
	@Column private int level;
	@Column private int experience;
	@Column private String homeworldId;
	
	@Column private Stats stats;
	
	@Column private int hitPoints;
	@Column private int psiPoints;
	@Column private int systemStrain;
	
	@Column private int attackBonus;
	@Column private Saves saves;
	
	@Column private List<Skill> skills;
	@Column private int unspentSkillPoints;
	
	// *******************************************************************************
	// * Getters/Setters
	// *******************************************************************************
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPlayerId() {
		return playerId;
	}
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getExperience() {
		return experience;
	}
	public void setExperience(int experience) {
		this.experience = experience;
	}
	public String getHomeworldId() {
		return homeworldId;
	}
	public void setHomeworldId(String homeworldId) {
		this.homeworldId = homeworldId;
	}
	public Stats getStats() {
		return stats;
	}
	public void setStats(Stats stats) {
		this.stats = stats;
	}
	public int getHitPoints() {
		return hitPoints;
	}
	public void setHitPoints(int hitPoints) {
		this.hitPoints = hitPoints;
	}
	public int getPsiPoints() {
		return psiPoints;
	}
	public void setPsiPoints(int psiPoints) {
		this.psiPoints = psiPoints;
	}
	public int getSystemStrain() {
		return systemStrain;
	}
	public void setSystemStrain(int systemStrain) {
		this.systemStrain = systemStrain;
	}
	public int getAttackBonus() {
		return attackBonus;
	}
	public void setAttackBonus(int attackBonus) {
		this.attackBonus = attackBonus;
	}
	public Saves getSaves() {
		return saves;
	}
	public void setSaves(Saves saves) {
		this.saves = saves;
	}
	public List<Skill> getSkills() {
		return skills;
	}
	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}
	public int getUnspentSkillPoints() {
		return unspentSkillPoints;
	}
	public void setUnspentSkillPoints(int unspentSkillPoints) {
		this.unspentSkillPoints = unspentSkillPoints;
	}
	
	// *******************************************************************************
	// * Misc.
	// *******************************************************************************
	
	@Override
	public String toString() {
		return String.format(
				"PlayerCharacter [id=%s, playerId=%s, name=%s, className=%s, level=%s, experience=%s, homeworldId=%s, stats=%s, hitPoints=%s, psiPoints=%s, systemStrain=%s, attackBonus=%s, saves=%s, skills=%s, unspentSkillPoints=%s]", //$NON-NLS-1$
				id, playerId, name, className, level, experience, homeworldId, stats, hitPoints, psiPoints, systemStrain, attackBonus, saves, skills, unspentSkillPoints);
	}
	
	
}
