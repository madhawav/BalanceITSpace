# Game Engine
The game engine consists of 4 packages.
1) [The Core Engine](coreengine): Provides an AbstractGame class which can be extended to implement a game. 
This class provides useful events such as onUpdate and onRender. Additionally, there are tools to access the Gravity sensor and the Vibrator.
2) [Graphics Engine](graphics): Provides an implementation of a Graphics Engine and a Sprite Engine. 
Provides a TextureAssetManager which enables sharing of texture resources across multiple owners. It also takes care of destruction of texture resources when the last ownership is revoked.
Provides a BasicShader implementation.
3) [Multi-scene Framework](multiscene): Extends AbstractGame to support serving of games with multiple scenes. (E.g. Game-play scene, Main menu scene). 
4) [UI Components](ui): Implements various UI elements such as ImageButton, Image, Label, Rectangle. 
Also provides a LayeredUI container element which allows stacking of UI elements into layers.
UIElementScene allows hosting of a UI element as a scene of a multi-scene game.
5) [Math Utils](math): Math utils related to 3D geometry such as Vector3.