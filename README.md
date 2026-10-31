# 🧪 Elemental Siege: Learn Through Battle

**Where chemistry knowledge meets elemental combat!** Defend your laboratory by turning correct answers into powerful attacks in this unique educational RPG.

## ⚔️ Core Gameplay Loop

1. **Choose Your Champions**: Select character cards with high Problem Solving, Wisdom, and Curiosity stats
2. **Enter Battle**: Face off against invading monsters in turn-based combat
3. **Answer to Attack**: Correct quiz questions launch elemental strikes (Fire, Water, Earth, Air)
4. **Learn from Mistakes**: Wrong answers trigger detailed explanations before your next attempt
5. **Level Up**: Earn XP, increase stats, and unlock stronger abilities as you progress

## 🚀 How to Play

```shell
# From the project directory:
cd src && javac *.java && java ElementalSiege && rm *.class && cd .. # and there you go...
```

<details>
<summary>⚠ <b>Prerequisites & Compatibility</b></summary>
<p style="font-size: 0.85em; margin-left: 20px; color: #57606a;">
  • Requires the <b>Java Development Kit (JDK 11+)</b> installed and added to your system path.<br>
  • Works out-of-the-box on <b>Linux</b>, <b>macOS</b>, <b>Windows CMD</b>, and <b>PowerShell 7+</b>.<br>
  • <i>Note for older Windows systems:</i> If you are using the default <b>Windows PowerShell 5.1</b>, the <code>&&</code> operator will cause an error. Please use CMD or upgrade to modern PowerShell.
</p>
</details>


## 🎮 Game Systems

### 💎 Character Development
- Four key attributes: Problem Solving, Wisdom, Curiosity, and Knowledge
- Level-based progression with increasing difficulty
- Save system preserves your lab's defenses between sessions

### ⚛️ Elemental Combat
- Match character elements against monster weaknesses
- Strategic card selection affects damage output
- Visual feedback shows attack effectiveness

### 📖 Educational Integration
- Chemistry-focused questions from curated databases
- Immediate feedback with explanations for incorrect answers
- Progressive difficulty as you advance through levels
- Tutorial system teaches both game mechanics and concepts

### 🖼️ Polished Presentation
- Custom-drawn card artwork and animations
- Smooth panel transitions with fade effects
- Audio feedback for actions and events
- Intuitive UI designed for extended play sessions

## 📂 Project Organization

```
src/
├── ElementalSiege.java      # Game launcher (JFrame)
├── BattlePanel.java         # Core combat system
├── QuestionPanel.java       # Quiz delivery engine
├── TutorialPanel.java       # Animated learning guide
├── GameData.java            # Question/character database
├── PageManager.java         # Screen navigation (CardLayout)
├── Sound.java               # Audio management
└── [20+ more panels/systems]

storedData/
├── Questions.txt            # Educational content database
├── CharacterImages/         # Visual assets for character cards
├── images/                  # Visual assets for graphical enhancement
└── Audio/                   # Game sounds and music

```

## 🎯 Who Should Play

- MV Chemistry Honors students wanting to reinforce chemistry concepts 
- Educators seeking engaging classroom tools
- Fans of RPG mechanics looking for educational twists
- Anyone who believes learning should feel like an adventure

**Elemental Siege proves that when you gamify education, players don't just learn—they want to keep playing.**


## 📚 References

All sources utilized in this project are documented in the [WORKS_CITED.md](./WORKS_CITED.md) file.

---

*Created by Daksh Agrawal & Dhikshitha Aiely*

<small>*Under the guidance of Mr. Yu - MVHS Java Programming (Period 7)*</small>
