# PSIonic Upgrades
Magic?? Tech?? Addons??

A mod by yrsegal. A 1.10 -> 1.12 and Kotlin -> Java port by quaternary.

Very unofficial, very broken, very unfinished.

Build status
============

***DON'T EVEN TRY!!!!!!***

What in heck
============

I miss Psionic Upgrades but I'm not comfortable dealing with Kotlin, and I'd rather not deal with the changes in LibLib since then anyways. So I am rewriting the whole mod in Java and removing the LibrarianLib dependency.

The repo is a bit of a mess, on purpose - I'm renaming each Kotlin file as I port it from `TheFile.kt` to `TheFileKt.kt`, and creating a new `TheFile.java`, so I can crossreference the original Kotlin source if need be. When I've ported every file, I will remove all the Kotlin files.

This is a weird way of working, and yes, it's dumb.

Original README feature list
=============================

Keep in mind that at the moment literally nothing you see will work.

## Items

- Wide-Band CAD Socket  
    A CAD socket that can accept a wide bandwidth, but only one bullet.
- Liquid-Ink CAD Colorizer  
    A dyable colorizer that can be 'drained' for a near-black colorizer.
- Inline Caster  
    A caster that has one spell slot and one empty slot. It uses your main CAD stats.
- Spell Magazine  
    Spell storage based on the socket used to craft it.
- CAD Cases  
    A portable storage that can store one CAD and one other spell holder. They can be placed on the ground or used in-hand.
    On the ground, they can be opened and closed with a shift-click, and can be interacted with while open.
- Flowsuits and Flowtools  
    Aesthetically upgraded versions of the Exosuit and Psimetal Tools that are plated with Ebony or Ivory.
- Flash Ring  
    A porta-programmer that takes a lot out of you to use.
- Exosuit Biotic Sensor
    A helmet sensor that senses life coming within 10 blocks from you.
- Gauss Rifle
    A weapon designed for fighting psimages, for those without a knack for programming. Nobody should be that powerful without a counter.
- Psionic Potions
    Potions which enhance or degrade Psi energy. Watch out, having both makes it drain even faster!
- Blaster CAD Assembly *Botania integration*  
    A CAD assembly with the stats of Psimetal. A CAD made with it can take Lenses and cast Mana Tricks at the base level. It can take a Lens Clip, and with it can cast Mana Tricks at the Alfheim level.

## Spell Pieces
### Entities 101
- Operator: List Size
    Gets the size of an Entity List.

### Flow Control
- Trick: Break Loop
    Acts like Trick: Die, except also breaks loopcasts and spell circles.

### Alternate Conjuration
- Trick: Conjure Pulsar  
    Conjures a redstone emitting block, similar to Trick: Conjure Block.
- Trick: Conjure Pulsar Sequence  
    Conjures a sequence of redstone emitting blocks, similar to Trick: Conjure Block Sequence.
- Trick: Conjure Pulsar Light  
    Conjures a redstone emitting light, similar to Trick: Conjure Light.
- Trick: Conjure Star  
    Conjures a light that emits a ray of particles along a given vector.

### Vector Operators II
- Operator: Planar Normal Vector  
    Gets an arbitrary perpendicular vector to an axial vector.
- Operator: Vector Rotate  
    Rotates a vector around any axis, by a given angle.
- Operator: Vector Strong Raycast  
    Raycasts similarly to Operator: Vector Raycast, except ignoring nonsolid blocks.
- Operator: Vector Axis Strong Raycast  
    Raycasts similarly to Operator: Vector Axis Raycast, except ignoring nonsolid blocks.
- Operator: Vector Fallback
    Replaces null and zero vectors with a fallback vector.
- Trick: Particle Trail  
    Creates a particle trail from a position, along a ray, for a given distance. No Psi cost, but increases potency.

### Mana Manipulation *Botania integration*
- Trick: Form Burst  
    Creates a mana burst from a position along a ray.  
    Requires: Blaster CAD Assembly
- Trick: Wild Pulse  
    Breaks grass and flowers similarly to the Drum of the Wild.  
    Requires: Mana Flow Enabler
- Trick: Canopy Pulse  
    Breaks grass and flowers similarly to the Drum of the Canopy.  
    Requires: Mana Flow Enabler
- Trick: Gathering Pulse  
    Breaks grass and flowers similarly to the Drum of the Wild.  
    Requires: Elven Mana Flow Enabler

## Misc. Features
- JEI compat with the Crafting tricks.
- `/psi-learn` and `/psi-unlearn` commands.
- Colored Flow Plates.
