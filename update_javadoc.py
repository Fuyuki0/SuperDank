import os
import re

docs = {
    "ItemRarity.java": "Defines the different levels of rarity for items in the game, such as Common, Rare, and Legendary.",
    "Rarity.java": "Interface or base class for defining the rarity characteristics of game items.",
    "GameObject.java": "The base class for all entities in the game, providing common properties like position and size.",
    "Obstacle.java": "Represents an obstacle in the game world that entities might collide with.",
    "CrossSlash.java": "A skill representing a cross-shaped slash attack.",
    "JumpingSlash.java": "A skill representing a jumping slash attack.",
    "Slash.java": "A basic skill representing a standard slash attack.",
    "Ultimate.java": "A powerful ultimate skill that deals significant damage.",
    "OrbitRock.java": "A weapon that summons rocks orbiting the player to damage nearby enemies.",
    "LightningWeapon.java": "A weapon that strikes enemies with lightning bolts.",
    "BoomerangWeapon.java": "A weapon that throws a boomerang projectile which returns to the player.",
    "Bow.java": "A ranged weapon that shoots arrows at enemies.",
    "Weapon.java": "The base abstract class for all player weapons, handling firing logic and upgrades.",
    "Aura.java": "A weapon that creates a damaging area of effect around the player.",
    "DataCore.java": "A core upgrade that enhances data processing or tech-based abilities.",
    "ChaosCore.java": "A core upgrade that introduces chaotic and unpredictable effects.",
    "DynamoCore.java": "A core upgrade that focuses on energy generation and electrical attacks.",
    "SomaticCore.java": "A core upgrade that enhances physical attributes and recovery.",
    "FractalCore.java": "A core upgrade that multiplies or splits projectiles and effects.",
    "OverClockCore.java": "A core upgrade that increases attack speed and reduces cooldowns.",
    "MatrixCore.java": "A core upgrade that manipulates space or enemy positioning.",
    "SurgeCore.java": "A core upgrade that provides bursts of power and damage.",
    "KineticCore.java": "A core upgrade that increases the physical force and knockback of attacks.",
    "ResonanceCore.java": "A core upgrade that amplifies damage based on consecutive hits.",
    "PlasmaCore.java": "A core upgrade that melts enemies with high-temperature energy.",
    "CryptoCore.java": "A core upgrade that provides defensive or evasive enhancements.",
    "BiocatalystCore.java": "A core upgrade that uses biological effects to poison or weaken enemies.",
    "OverHeatCore.java": "A core upgrade that sacrifices defense or health for extreme damage output.",
    "VectorCore.java": "A core upgrade that enhances projectile speed and direction.",
    "EntropyCore.java": "A core upgrade that causes lingering deterioration on enemies.",
    "SiphonCore.java": "A core upgrade that heals the player based on damage dealt.",
    "WeaponType.java": "Enumeration of the different categories or types of weapons available.",
    "Sword.java": "A melee weapon used for close-range combat.",
    "EngineOverload.java": "An equipment item that temporarily boosts speed and damage.",
    "FinalWeapon.java": "An extremely powerful equipment item representing the ultimate arsenal.",
    "ServoMotor.java": "An equipment item that enhances movement speed or attack rate.",
    "HexEditor.java": "An equipment item that modifies or corrupts enemy data.",
    "PhantomDrive.java": "An equipment item that provides evasion or phasing abilities.",
    "NecroticHeart.java": "An equipment item that grants power at the cost of health or causes decay.",
    "SoulHarvester.java": "An equipment item that gains strength by defeating enemies.",
    "ParasiticGas.java": "An equipment item that emits a toxic cloud damaging nearby enemies.",
    "MutatedBrain.java": "An equipment item that enhances skill effectiveness or reduces cooldowns.",
    "AdrenalSyringe.java": "An equipment item that provides a burst of healing or emergency power.",
    "TungstenPlating.java": "An equipment item that significantly increases armor and defense.",
    "MagneticRepulsor.java": "An equipment item that pushes enemies away from the player.",
    "DataFragment.java": "An equipment item providing incremental tech bonuses.",
    "DeepestVoid.java": "An equipment item that utilizes black hole or gravity mechanics.",
    "MagnetRing.java": "An equipment item that increases the pickup range for collectables.",
    "Doomsday.java": "An equipment item that unleashes massive destruction when triggered.",
    "TitaniumArmor.java": "A defensive equipment item that reduces incoming damage.",
    "NeuroAmplifier.java": "An equipment item that boosts mental or magical capabilities.",
    "EnergyDrink.java": "A power-up item that restores energy or stamina.",
    "Steak.java": "A power-up item that restores a large amount of health.",
    "ItemManager.java": "Manages the spawning, updating, and logic of all items in the game.",
    "Experience.java": "A collectable item that grants experience points to the player upon pickup.",
    "Chest.java": "A collectable object that drops valuable items or power-ups when opened.",
    "PlayerTrail.java": "A visual effect class representing the trail left behind by the player.",
    "Player.java": "The main class representing the user-controlled character, managing health, stats, and input.",
    "DamageText.java": "A visual indicator that displays the amount of damage dealt to an entity.",
    "Boss.java": "An advanced enemy with unique attacks, higher health, and distinct phases.",
    "Charger.java": "An enemy that aggressively charges towards the player to deal damage.",
    "CompareEnemies.java": "A utility class for comparing and sorting enemies, often used for targeting.",
    "Enemy.java": "The base abstract class for all hostile entities in the game.",
    "Projectile.java": "The base class for all moving projectiles fired by weapons or skills.",
    "Explosion.java": "A class representing an explosive effect that damages entities in an area.",
    "Rock.java": "A basic projectile representing a thrown rock or fireball.",
    "Boomerang.java": "A projectile that travels outward and then returns towards its origin.",
    "Arrow.java": "A linear projectile fired by bows or similar weapons.",
    "LightningEffect.java": "A visual and damaging effect representing a lightning strike.",
    "Renderable.java": "An interface for any game object that can be drawn on the screen.",
    "ActiveSkill.java": "An interface or base class for skills that require player activation.",
    "Item.java": "The base interface or class for all items that can be collected or equipped.",
    "Collectable.java": "An interface for items that the player can pick up in the game world.",
    "GameLauncher.java": "The entry point for the application, initializing the game window and assets.",
    "GameAssets.java": "A centralized manager for loading and providing all game resources like images and fonts.",
    "Controller.java": "Handles user input from the keyboard or mouse to control the game.",
    "World.java": "The main container class representing the game state, including entities, map, and collision.",
    "GameEngine.java": "The core engine that runs the game loop, handling updates and rendering.",
    "Camera.java": "Manages the viewpoint of the game, keeping the player focused on the screen.",
    "Main.java": "The bootstrap class for the JavaFX application.",
    "SoundManager.java": "Handles the loading and playback of all audio effects and background music."
}

def process_file(filepath):
    filename = os.path.basename(filepath)
    if filename not in docs:
        return
    
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
        
    # Check if there's already a class-level javadoc
    # This is a simple heuristic: if "/**" exists right before the class declaration
    # However, sometimes there are imports. 
    # Let's search for the class declaration.
    
    pattern = re.compile(r'^(public\s+|abstract\s+)*(class|interface|enum)\s+(\w+).*?$', re.MULTILINE)
    match = pattern.search(content)
    
    if not match:
        return
        
    class_start = match.start()
    
    # Check if there's a /** right before it
    text_before = content[:class_start].strip()
    if text_before.endswith("*/") and "/**" in text_before.split("\n")[-1] or "/**" in text_before[-50:]:
        # Already has some javadoc
        # Wait, GameAssets has javadoc now because I generated it in chat, but did I write it to file? No.
        pass
        
    # If the text immediately preceding the class declaration is not a javadoc, we insert it.
    if not text_before.endswith("*/"):
        doc_string = f"/**\n * {docs[filename]}\n */\n"
        # We need to insert it at class_start
        # Wait, what if there are annotations like @Override? Very rare for classes.
        new_content = content[:class_start] + doc_string + content[class_start:]
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(new_content)
            
for root, dirs, files in os.walk('src/main/java'):
    for file in files:
        if file.endswith('.java'):
            process_file(os.path.join(root, file))

print("Done updating Javadocs.")
