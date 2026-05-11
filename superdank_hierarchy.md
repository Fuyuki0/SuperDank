# SuperDank — Full Class Hierarchy

> **Legend:**  `<<interface>>` · `<<abstract>>` · `<<enum>>` · `[ implements ]`

---

## 🧩 Interfaces (the contracts)

```
<<interface>> Renderable
    └── <<interface>> ActiveSkill   (extends Renderable)

<<interface>> Collectable           (standalone)
<<interface>> Item                  (standalone)
```

---

## 🏗️ Core Game Engine (no inheritance — standalone classes)

```
MyGame/
├── GameLauncher                    (entry point, runs Main)
│
└── Game/
    ├── Main          extends Application (JavaFX)
    ├── GameEngine                  (master loop, rendering, input)
    ├── GameAssets                  (image/font loader)
    ├── World                       (spawning, collision, game state)
    ├── Controller                  (key/mouse input bindings)
    ├── Camera                      (viewport tracking)
    └── SoundManager                (audio playback)
```

---

## 🎮 GameObject Hierarchy  (`<<abstract>> GameObject implements Renderable`)

```
<<abstract>> GameObject  [implements Renderable]
│
├── 👾 Enemies/
│   ├── Enemy  extends GameObject
│   │   ├── Boss     extends Enemy          (boss phases, telegraphs)
│   │   └── Charger  extends Enemy          (rush-type enemy)
│   └── CompareEnemies                      (utility — no inheritance)
│
├── 🧑 Player/
│   ├── Player       extends GameObject     (main player entity)
│   └── PlayerTrail  extends GameObject     (visual trail effect)
│
├── 🔫 Projectile/
│   ├── <<abstract>> Projectile  extends GameObject
│   │   ├── Arrow      extends Projectile
│   │   ├── Boomerang  extends Projectile
│   │   ├── Rock       extends Projectile
│   │   └── LightningEffect  extends Projectile
│   └── Explosion      extends GameObject   (not a Projectile subclass)
│
├── 💥 Skill/  [all implement ActiveSkill → also Renderable]
│   ├── Slash         implements ActiveSkill
│   ├── CrossSlash    implements ActiveSkill
│   ├── JumpingSlash  implements ActiveSkill
│   └── Ultimate      implements ActiveSkill
│
├── ⚔️ Weapon/
│   ├── <<abstract>> Weapon  [implements Renderable]
│   │   ├── Sword          extends Weapon
│   │   ├── Bow            extends Weapon
│   │   ├── BoomerangWeapon extends Weapon
│   │   ├── LightningWeapon extends Weapon
│   │   ├── OrbitRock      extends Weapon
│   │   ├── Aura           extends Weapon
│   │   └── Cores/         (all extend Weapon)
│   │       ├── BiocatalystCore
│   │       ├── ChaosCore
│   │       ├── CryptoCore
│   │       ├── DataCore
│   │       ├── DynamoCore
│   │       ├── EntropyCore
│   │       ├── FractalCore
│   │       ├── KineticCore
│   │       ├── MatrixCore
│   │       ├── OverClockCore
│   │       ├── OverHeatCore
│   │       ├── PlasmaCore
│   │       ├── ResonanceCore
│   │       ├── SiphonCore
│   │       ├── SomaticCore
│   │       ├── SurgeCore
│   │       └── VectorCore
│   └── <<enum>> WeaponType          (SWORD, BOW, etc.)
│
├── 🎒 Items/
│   ├── ItemManager                   (spawning logic — no inheritance)
│   │
│   ├── Collectable/  [extend GameObject + implement Collectable + Renderable]
│   │   ├── Experience  extends GameObject  [implements Collectable, Renderable]
│   │   └── Chest       extends GameObject  [implements Collectable, Renderable]
│   │
│   ├── PowerUp/  [extend GameObject + implement Collectable + Renderable]
│   │   ├── Steak        extends GameObject  [implements Item, Collectable, Renderable]
│   │   └── EnergyDrink  extends GameObject  [implements Collectable, Renderable]
│   │
│   └── Equipment/  [all implement Item only — passive stat modifiers]
│       ├── AdrenalSyringe    implements Item
│       ├── DataFragment      implements Item
│       ├── DeepestVoid       implements Item
│       ├── Doomsday          implements Item
│       ├── EngineOverload    implements Item
│       ├── FinalWeapon       implements Item
│       ├── HexEditor         implements Item
│       ├── MagnetRing        implements Item
│       ├── MagneticRepulsor  implements Item
│       ├── MutatedBrain      implements Item
│       ├── NecroticHeart     implements Item
│       ├── NeuroAmplifier    implements Item
│       ├── ParasiticGas      implements Item
│       ├── PhantomDrive      implements Item
│       ├── ServoMotor        implements Item
│       ├── SoulHarvester     implements Item
│       ├── TitaniumArmor     implements Item
│       └── TungstenPlating   implements Item
│
├── Obstacle     extends GameObject   (walls / terrain)
└── DamageText   extends GameObject   (floating numbers)
```

---

## 🎲 Rarity System (standalone)

```
Rarity/
├── <<enum>> Rarity         (COMMON, RARE, EPIC, LEGENDARY…)
└── ItemRarity              (maps Item → Rarity — utility class)
```

---

## 🔗 Key Relationship Summary

| What | Connects to | How |
|---|---|---|
| Every drawable game object | `Renderable` | via `GameObject implements Renderable` |
| Skills | `ActiveSkill` → `Renderable` | implements both via single interface |
| Weapons | `Renderable` | directly `implements Renderable` (not a GameObject!) |
| Collectable items | `GameObject` + `Collectable` + `Renderable` | multi-interface |
| Equipment | `Item` only | pure passive data — no position/drawing needed |
| `Boss` | `Enemy` → `GameObject` → `Renderable` | 3-level deep chain |
