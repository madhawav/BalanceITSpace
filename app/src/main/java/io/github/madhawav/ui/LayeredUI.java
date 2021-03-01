package io.github.madhawav.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.microedition.khronos.opengles.GL10;

public class LayeredUI extends AbstractUIElement {
    private boolean started;
    private List<AbstractUIElement> elements;
    public LayeredUI(GraphicsContext graphicsContext){
        super(graphicsContext);
        this.elements = new ArrayList<>();
        started = false;
    }

    public void addElement(AbstractUIElement element){
        if(this.isFinished())
            throw new IllegalStateException("LayeredUI has Finished");
        this.elements.add(element);
        registerModule(element);

        if(started)
            element.onStart();
    }

    public void finishElement(AbstractUIElement element){
        if(this.isFinished())
            throw new IllegalStateException("LayeredUI has Finished");

        if(!this.elements.contains(element))
            throw new NoSuchElementException();
        unregisterModule(element);
        this.elements.remove(element);
        element.finish();
    }

    public AbstractUIElement get(int index){
        return this.elements.get(index);
    }

    @Override
    public void onStart() {
        this.started = true;
        this.elements.forEach(AbstractUIElement::onStart);
    }

    @Override
    public void onUpdate(double elapsedSec) {
        this.elements.forEach((element -> element.onUpdate(elapsedSec)));
    }

    @Override
    public void onRender(GL10 gl10) {
        if(!isVisible())
            return;
        float preservedOpacity = this.getGraphicsContext().getOpacity();
        this.getGraphicsContext().setOpacity(getOpacity() * preservedOpacity);

        this.elements.forEach((element -> element.onRender(gl10)));

        this.getGraphicsContext().setOpacity(preservedOpacity);
    }

    @Override
    public boolean onTouchDown(float x, float y) {
        if(!isVisible())
            return false;

        for(int i = elements.size() - 1; i >= 0; i--){
            if(elements.get(i).onTouchDown(x,y))
                return true;
        }
        return false;
    }

    @Override
    public boolean onTouchMove(float x, float y) {
        if(!isVisible())
            return false;

        for(int i = elements.size() - 1; i >= 0; i--){
            if(elements.get(i).onTouchMove(x,y))
                return true;
        }
        return false;
    }

    @Override
    public boolean onTouchReleased(float x, float y) {
        if(!isVisible())
            return false;

        for(int i = elements.size() - 1; i >= 0; i--){
            if(elements.get(i).onTouchReleased(x,y))
                return true;
        }
        return false;
    }
}
