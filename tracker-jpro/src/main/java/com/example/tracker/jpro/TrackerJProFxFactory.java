/*
 * Tracker JPro web client.
 */

package com.example.tracker.jpro;

import com.example.tracker.jpro.session.UserScope;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import org.tentackle.common.Service;
import org.tentackle.fx.FxFactory;
import org.tentackle.fx.FxRuntimeException;
import org.tentackle.fx.FxUtilities;
import org.tentackle.fx.rdc.RdcFxFactory;
import org.tentackle.log.Logger;

/**
 * FX factory for the web client.
 * <p>
 * Creates stages the way JPro and a multi-user JVM need them:
 * <ul>
 *   <li><strong>without an owner.</strong> {@code WebAPI.openStageAsTab(Stage)} rejects an owned
 *       stage ({@code "The owner of the stage must be null"}, see {@code WebAPIImpl.registerStage}),
 *       because the stage becomes a browser tab of its own rather than a child window.</li>
 *   <li><strong>without modality.</strong> {@link Modality#APPLICATION_MODAL} blocks every window of
 *       the JVM — which here means every window of <em>every</em> user. {@link Modality#WINDOW_MODAL}
 *       is meaningless without an owner. Nothing in Tentackle depends on the blocking itself: it
 *       never calls {@code showAndWait}, dialogs are non-blocking and answer through callbacks.</li>
 * </ul>
 * The owner is not simply thrown away: it is what tells us which user the new stage belongs to, so
 * the stage is bound to the owner's {@link UserScope} instead.
 *
 * @see TrackerJProFxUtilities#show(Stage)
 */
@Service(FxFactory.class)
public class TrackerJProFxFactory extends RdcFxFactory {

  private static final Logger LOGGER = Logger.get();

  /**
   * Creates the factory.
   */
  public TrackerJProFxFactory() {
    // see -Xlint:missing-explicit-ctor since Java 16
  }

  /**
   * {@inheritDoc}
   * <p>
   * Overridden to create an ownerless, non-modal stage bound to the owner's user.
   */
  @Override
  public Stage createStage(StageStyle stageStyle, Modality modality, Object owner) {
    UserScope scope = scopeOf(owner);
    if (scope == null) {
      // no user yet (the primary stage before login) -> let JavaFX/Tentackle do their thing
      return super.createStage(stageStyle, modality, owner);
    }
    Stage stage = super.createStage(stageStyle, Modality.NONE, null);
    UserScope.bindWindow(stage, scope);
    LOGGER.fine("stage created for {0}", scope);
    return stage;
  }


  /**
   * Determines the scope an owner belongs to.
   *
   * @param owner a window, scene or node, null if unknown
   * @return the scope, null if it cannot be determined
   */
  protected UserScope scopeOf(Object owner) {
    if (owner != null) {
      try {
        Window window = FxUtilities.getInstance().getWindow(owner);
        UserScope scope = UserScope.of(window);
        if (scope != null) {
          return scope;
        }
      }
      catch (FxRuntimeException fx) {
        LOGGER.fine("cannot determine the window of " + owner, fx);
      }
    }
    return UserScope.current();
  }

}
