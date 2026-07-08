# Obsession Mod — Implementation Spec

## 1. Vision & Non-Goals
- One-paragraph pitch tied to film beats
- Non-goals: no jump-scare mobs, no per-player desync, no meta-horror without consent

## 2. Glossary
- Spirit, Obsession Level, Phase, Event, Wisher, Catalog Event, Meta Layer

## 3. Player Experience Overview
- Timeline diagram (Phase 0 → Finale)
- What players see/hear/feel each phase
- Film beat → Minecraft mechanic mapping table

## 4. Architecture
### 4.1 Mod layout (Fabric modules: common / server / client)
### 4.2 Spirit Brain (server-side state machine)
### 4.3 AI Integration Layer
  - Prompt contract, context window, memory schema
  - Allowed outputs (JSON schema only — never raw code/commands)
  - Event catalog the AI picks from
  - Fallback when AI unavailable
### 4.4 Networking (multiplayer sync packets)
### 4.5 Persistence (world save data for phase, memory, opt-in flags)

## 5. Obsession Phase System
For each phase (0–N):
- Duration / trigger to advance
- Allowed event types
- World-edit budget (blocks per tick, radius)
- UI/audio permissions
- Example film parallel

Phases mapped to film:
| Phase | Name | Film beat | MC mechanics |
|-------|------|-----------|--------------|
| 0 | Dormant | Pre-wish | Normal world |
| 1 | The Wish | One Wish Willow | Configurable trigger item/ritual |
| 2 | Devotion | Nikki suddenly loves Bear | Gifts, helpful builds, sweet messages |
| 3 | The Pet | Sandy + oxycodone | Tamed pet dies / acts wrong |
| 4 | Don't Leave | Duct-taped door, pause screen | Door seal, DontLeaveMeScreen, bed watch |
| 5 | Wrong | Nikki not herself | Mob possession, villager dialogue glitches |
| 6 | Isolation | Sarah/Ian distance | Kill pets, block paths, fake notes in chests |
| 7 | Witnessed | Sandwich note / paw | Explicit in-world horror events |
| 8 | Trapped | Can't escape house | Heavy world edits, fake exits, pocket rooms |
| 9 | The Way Out | Break the willow | Quest chain unlocks |
| 10 | Finale | Overdose / wish ends | AI confrontation + scripted resolution |

## 6. Event Catalog (machine-readable)
JSON/YAML list of every event:
- id, phase_min, phase_max, weight, multiplayer_safe
- server_actions[] (typed, validated)
- client_effects[] (sound, screen, TTS line, particle)
- ai_prompt_hint (when AI should prefer this event)
- content_flags[] (pet_harm, gore_theme, meta)

## 7. AI Contract
- System prompt personality (obsessive, possessive, film-accurate tone)
- Input: player snapshot, phase, recent events, memory
- Output schema: { "event_id": "...", "dialogue": "...", "tts_line": "...", "escalate": bool }
- Guardrails: banned topics, rate limits, never target real players with slurs, etc.
- Memory: what the spirit "remembers" about each player

## 8. Client Systems
- Screen overlays (DontLeaveMeScreen, fake disconnect, bed-watcher vignette)
- TTS pipeline (local vs cloud TTS — separate from LLM decision)
- Sound library hooks
- Meta-horror module (consent-gated, client-only)

## 9. Meta-Horror Layer (Opt-In)
- First-launch consent screen (stored in config)
- Allowed actions whitelist: write `.txt` to agreed folder, request focus, NOT keylogging
- Reversible / disable in config
- Clear uninstall behavior

## 10. Multiplayer Rules
- Authority model
- Who is "the wisher"
- Event broadcast vs targeted effects
- Late joiner behavior

## 11. Config & Commands
- `/obsession` admin commands (force phase, reset, debug)
- Server config: AI provider, intensity toggles, phase durations
- Per-event toggles for pet harm, TTS, meta layer

## 12. Implementation Phases (for the agent)
### Milestone 1: Core loop (phase state machine, no AI)
### Milestone 2: Event catalog + 5 starter events
### Milestone 3: Client effects (screen, sound, TTS stub)
### Milestone 4: AI picker integration
### Milestone 5: Multiplayer sync
### Milestone 6: Break-free quest chain
### Milestone 7: Meta-horror opt-in
### Milestone 8: Polish + content warnings screen

## 13. Testing Checklist
- Single-player full arc
- 2-player LAN sync
- AI offline fallback
- Consent declined → meta disabled
- Pet harm toggle off

## 14. File & Package Conventions
- Match existing: `world.smallwoken.obsession`
- New packages: `.spirit`, `.events`, `.ai`, `.network`, `.meta`

## 15. References
- Film beat list (your summary)
- Existing files: ObsessionClient.java, DontLeaveMeScreen.java