/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camunda.bpm.dmn.engine.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionEvaluationListener;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionTableEvaluationListener;
import org.camunda.bpm.dmn.engine.impl.el.DefaultScriptEngineResolver;
import org.camunda.bpm.dmn.engine.impl.el.JuelElProvider;
import org.camunda.bpm.dmn.engine.impl.metrics.DefaultEngineMetricCollector;
import org.camunda.bpm.dmn.engine.impl.spi.el.DmnScriptEngineResolver;
import org.camunda.bpm.dmn.engine.impl.spi.el.ElProvider;
import org.camunda.bpm.dmn.engine.impl.spi.transform.DmnTransformer;
import org.camunda.bpm.dmn.engine.impl.transform.DefaultDmnTransformer;
import org.camunda.bpm.dmn.engine.spi.DmnEngineMetricCollector;
import org.camunda.bpm.dmn.feel.impl.FeelEngine;
import org.camunda.bpm.dmn.feel.impl.FeelEngineFactory;
import org.camunda.bpm.dmn.feel.impl.juel.FeelEngineFactoryImpl;
import org.camunda.bpm.model.dmn.impl.DmnModelConstants;

public class DefaultDmnEngineConfiguration extends DmnEngineConfiguration {

  public static final String FEEL_EXPRESSION_LANGUAGE = DmnModelConstants.FEEL_NS;
  public static final String FEEL_EXPRESSION_LANGUAGE_ALTERNATIVE = "feel";
  public static final String JUEL_EXPRESSION_LANGUAGE = "juel";

  protected DmnEngineMetricCollector engineMetricCollector;

  protected List<DmnDecisionTableEvaluationListener> customPreDecisionTableEvaluationListeners = new ArrayList<DmnDecisionTableEvaluationListener>();
  protected List<DmnDecisionTableEvaluationListener> customPostDecisionTableEvaluationListeners = new ArrayList<DmnDecisionTableEvaluationListener>();
  protected List<DmnDecisionTableEvaluationListener> decisionTableEvaluationListeners;

  // Decision evaluation listeners
  protected List<DmnDecisionEvaluationListener> decisionEvaluationListeners;
  protected List<DmnDecisionEvaluationListener> customPreDecisionEvaluationListeners = new ArrayList<DmnDecisionEvaluationListener>();
  protected List<DmnDecisionEvaluationListener> customPostDecisionEvaluationListeners = new ArrayList<DmnDecisionEvaluationListener>();

  protected DmnScriptEngineResolver scriptEngineResolver;
  protected ElProvider elProvider;
  protected FeelEngineFactory feelEngineFactory;
  protected FeelEngine feelEngine;

  protected String defaultInputExpressionExpressionLanguage = JUEL_EXPRESSION_LANGUAGE;
  protected String defaultInputEntryExpressionLanguage = FEEL_EXPRESSION_LANGUAGE;
  protected String defaultOutputEntryExpressionLanguage = JUEL_EXPRESSION_LANGUAGE;

  protected DmnTransformer transformer = new DefaultDmnTransformer();

  public DmnEngine buildEngine() {
    init();
    return new DefaultDmnEngine(this);
  }

  public void init() {
    initMetricCollector();
    initDecisionTableEvaluationListener();
    initDecisionEvaluationListener();
    initScriptEngineResolver();
    initElProvider();
    initFeelEngine();
  }

  protected void initMetricCollector() {
    if (engineMetricCollector == null) {
      engineMetricCollector = new DefaultEngineMetricCollector();
    }
  }

  protected void initDecisionTableEvaluationListener() {
    List<DmnDecisionTableEvaluationListener> listeners = new ArrayList<DmnDecisionTableEvaluationListener>();
    if (customPreDecisionTableEvaluationListeners != null && !customPreDecisionTableEvaluationListeners.isEmpty()) {
      listeners.addAll(customPreDecisionTableEvaluationListeners);
    }
    listeners.addAll(getDefaultDmnDecisionTableEvaluationListeners());
    if (customPostDecisionTableEvaluationListeners != null && !customPostDecisionTableEvaluationListeners.isEmpty()) {
      listeners.addAll(customPostDecisionTableEvaluationListeners);
    }
    decisionTableEvaluationListeners = listeners;
  }

  protected void initDecisionEvaluationListener() {
    List<DmnDecisionEvaluationListener> listeners = new ArrayList<DmnDecisionEvaluationListener>();
    if (customPreDecisionEvaluationListeners != null && !customPreDecisionEvaluationListeners.isEmpty()) {
      listeners.addAll(customPreDecisionEvaluationListeners);
    }
    
    if (customPostDecisionEvaluationListeners != null && !customPostDecisionEvaluationListeners.isEmpty()) {
      listeners.addAll(customPostDecisionEvaluationListeners);
    }
    decisionEvaluationListeners = listeners;
  }

  protected Collection<? extends DmnDecisionTableEvaluationListener> getDefaultDmnDecisionTableEvaluationListeners() {
    List<DmnDecisionTableEvaluationListener> defaultListeners = new ArrayList<DmnDecisionTableEvaluationListener>();
    defaultListeners.add(engineMetricCollector);
    return defaultListeners;
  }

  protected void initElProvider() {
    if(elProvider == null) {
      elProvider = new JuelElProvider();
    }
  }

  protected void initScriptEngineResolver() {
    if (scriptEngineResolver == null) {
      scriptEngineResolver = new DefaultScriptEngineResolver();
    }
  }

  protected void initFeelEngine() {
    if (feelEngineFactory == null) {
      feelEngineFactory = new FeelEngineFactoryImpl();
    }

    if (feelEngine == null) {
      feelEngine = feelEngineFactory.createInstance();
    }
  }

  public DmnEngineMetricCollector getEngineMetricCollector() {
    return engineMetricCollector;
  }

  public void setEngineMetricCollector(DmnEngineMetricCollector engineMetricCollector) {
    this.engineMetricCollector = engineMetricCollector;
  }

  public DefaultDmnEngineConfiguration engineMetricCollector(DmnEngineMetricCollector engineMetricCollector) {
    setEngineMetricCollector(engineMetricCollector);
    return this;
  }

  public List<DmnDecisionTableEvaluationListener> getCustomPreDecisionTableEvaluationListeners() {
    return customPreDecisionTableEvaluationListeners;
  }

  public void setCustomPreDecisionTableEvaluationListeners(List<DmnDecisionTableEvaluationListener> decisionTableEvaluationListeners) {
    this.customPreDecisionTableEvaluationListeners = decisionTableEvaluationListeners;
  }

  public DefaultDmnEngineConfiguration customPreDecisionTableEvaluationListeners(List<DmnDecisionTableEvaluationListener> decisionTableEvaluationListeners) {
    setCustomPreDecisionTableEvaluationListeners(decisionTableEvaluationListeners);
    return this;
  }

  public List<DmnDecisionTableEvaluationListener> getCustomPostDecisionTableEvaluationListeners() {
    return customPostDecisionTableEvaluationListeners;
  }

  public void setCustomPostDecisionTableEvaluationListeners(List<DmnDecisionTableEvaluationListener> decisionTableEvaluationListeners) {
    this.customPostDecisionTableEvaluationListeners = decisionTableEvaluationListeners;
  }

  public DefaultDmnEngineConfiguration customPostDecisionTableEvaluationListeners(List<DmnDecisionTableEvaluationListener> decisionTableEvaluationListeners) {
    setCustomPostDecisionTableEvaluationListeners(decisionTableEvaluationListeners);
    return this;
  }

  public List<DmnDecisionEvaluationListener> getCustomPreDecisionEvaluationListeners() {
    return customPreDecisionEvaluationListeners;
  }

  public void setCustomPreDecisionEvaluationListeners(List<DmnDecisionEvaluationListener> decisionEvaluationListeners) {
    this.customPreDecisionEvaluationListeners = decisionEvaluationListeners;
  }

  public DefaultDmnEngineConfiguration customPreDecisionEvaluationListeners(List<DmnDecisionEvaluationListener> decisionEvaluationListeners) {
    setCustomPreDecisionEvaluationListeners(decisionEvaluationListeners);
    return this;
  }

  public List<DmnDecisionEvaluationListener> getCustomPostDecisionEvaluationListeners() {
    return customPostDecisionEvaluationListeners;
  }

  public void setCustomPostDecisionEvaluationListeners(List<DmnDecisionEvaluationListener> decisionEvaluationListeners) {
    this.customPostDecisionEvaluationListeners = decisionEvaluationListeners;
  }

  public DefaultDmnEngineConfiguration customPostDecisionEvaluationListeners(List<DmnDecisionEvaluationListener> decisionEvaluationListeners) {
    setCustomPostDecisionEvaluationListeners(decisionEvaluationListeners);
    return this;
  }
  /**
   * The list of decision table evaluation listeners of the configuration. Contains
   * the pre, default and post decision table evaluation listeners. Is set during
   * the build of an engine.
   *
   * @return the list of decision table evaluation listeners
   */
  public List<DmnDecisionTableEvaluationListener> getDecisionTableEvaluationListeners() {
    return decisionTableEvaluationListeners;
  }

  /**
   * The list of decision evaluation listeners of the configuration. Contains
   * the pre, default and post decision evaluation listeners. Is set during
   * the build of an engine.
   *
   * @return the list of decision table evaluation listeners
   */
  public List<DmnDecisionEvaluationListener> getDecisionEvaluationListeners() {
    return decisionEvaluationListeners;
  }

  /**
   * @return the script engine resolver
   */
  public DmnScriptEngineResolver getScriptEngineResolver() {
    return scriptEngineResolver;
  }

  /**
   * Set the script engine resolver which is used by the engine to get
   * an instance of a script engine to evaluated expressions.
   *
   * @param scriptEngineResolver the script engine resolver
   */
  public void setScriptEngineResolver(DmnScriptEngineResolver scriptEngineResolver) {
    this.scriptEngineResolver = scriptEngineResolver;
  }

  /**
   * Set the script engine resolver which is used by the engine to get
   * an instance of a script engine to evaluated expressions.
   *
   * @param scriptEngineResolver the script engine resolver
   * @return this
   */
  public DefaultDmnEngineConfiguration scriptEngineResolver(DmnScriptEngineResolver scriptEngineResolver) {
    setScriptEngineResolver(scriptEngineResolver);
    return this;
  }

  /**
   * @return the el provider
   */
  public ElProvider getElProvider() {
    return elProvider;
  }

  /**
   * Set the el provider which is used by the engine to
   * evaluate an el expression.
   *
   * @param elProvider the el provider
   */
  public void setElProvider(ElProvider elProvider) {
    this.elProvider = elProvider;
  }

  /**
   * Set the el provider which is used by the engine to
   * evaluate an el expression.
   *
   * @param elProvider the el provider
   * @return this
   */
  public DefaultDmnEngineConfiguration elProvider(ElProvider elProvider) {
    setElProvider(elProvider);
    return this;
  }

  /**
   * @return the factory is used to create a {@link FeelEngine}
   */
  public FeelEngineFactory getFeelEngineFactory() {
    return feelEngineFactory;
  }

  /**
   * Set the factory to create a {@link FeelEngine}
   *
   * @param feelEngineFactory the feel engine factory
   */
  public void setFeelEngineFactory(FeelEngineFactory feelEngineFactory) {
    this.feelEngineFactory = feelEngineFactory;
    this.feelEngine = null; // clear cached FEEL engine
  }

  /**
   * Set the factory to create a {@link FeelEngine}
   *
   * @param feelEngineFactory the feel engine factory
   * @return this
   */
  public DefaultDmnEngineConfiguration feelEngineFactory(FeelEngineFactory feelEngineFactory) {
    setFeelEngineFactory(feelEngineFactory);
    return this;
  }

  /**
   * The feel engine used by the engine. Is initialized during the build of
   * the engine.
   *
   * @return the feel engine
   */
  public FeelEngine getFeelEngine() {
    return feelEngine;
  }

  /**
   * @return the default expression language for input expressions
   */
  public String getDefaultInputExpressionExpressionLanguage() {
    return defaultInputExpressionExpressionLanguage;
  }

  /**
   * Set the default expression language which is used to evaluate input expressions.
   * It is used for all input expressions which do not have a expression
   * language set.
   *
   * @param expressionLanguage the default expression language for input expressions
   */
  public void setDefaultInputExpressionExpressionLanguage(String expressionLanguage) {
    this.defaultInputExpressionExpressionLanguage = expressionLanguage;
  }

  /**
   * Set the default expression language which is used to evaluate input expressions.
   * It is used for all input expressions which do not have a expression
   * language set.
   *
   * @param expressionLanguage the default expression language for input expressions
   * @return this configuration
   */
  public DefaultDmnEngineConfiguration defaultInputExpressionExpressionLanguage(String expressionLanguage) {
    setDefaultInputExpressionExpressionLanguage(expressionLanguage);
    return this;
  }

  /**
   * @return the default expression language for input entries
   */
  public String getDefaultInputEntryExpressionLanguage() {
    return defaultInputEntryExpressionLanguage;
  }

  /**
   * Set the default expression language which is used to evaluate input entries.
   * It is used for all input entries which do not have a expression
   * language set.
   *
   * @param expressionLanguage the default expression language for input entries
   */
  public void setDefaultInputEntryExpressionLanguage(String expressionLanguage) {
    this.defaultInputEntryExpressionLanguage = expressionLanguage;
  }

  /**
   * Set the default expression language which is used to evaluate input entries.
   * It is used for all input entries which do not have a expression
   * language set.
   *
   * @param expressionLanguage the default expression language for input entries
   * @return this configuration
   */
  public DefaultDmnEngineConfiguration defaultInputEntryExpressionLanguage(String expressionLanguage) {
    setDefaultInputEntryExpressionLanguage(expressionLanguage);
    return this;
  }

  /**
   * @return the default expression language for output entries
   */
  public String getDefaultOutputEntryExpressionLanguage() {
    return defaultOutputEntryExpressionLanguage;
  }

  /**
   * Set the default expression language which is used to evaluate output entries.
   * It is used for all output entries which do not have a expression
   * language set.
   *
   * @param expressionLanguage the default expression language for output entries
   */
  public void setDefaultOutputEntryExpressionLanguage(String expressionLanguage) {
    this.defaultOutputEntryExpressionLanguage = expressionLanguage;
  }

  /**
   * Set the default expression language which is used to evaluate output entries.
   * It is used for all output entries which do not have a expression
   * language set.
   *
   * @param expressionLanguage the default expression language for output entries
   * @return this configuration
   */
  public DefaultDmnEngineConfiguration defaultOutputEntryExpressionLanguage(String expressionLanguage) {
    setDefaultOutputEntryExpressionLanguage(expressionLanguage);
    return this;
  }

  /**
   * @return the DMN transformer
   */
  public DmnTransformer getTransformer() {
    return transformer;
  }

  /**
   * Set the DMN transformer used to transform the DMN model.
   *
   * @param transformer the DMN transformer
   */
  public void setTransformer(DmnTransformer transformer) {
    this.transformer = transformer;
  }

  /**
   * Set the DMN transformer used to transform the DMN model.
   *
   * @param transformer the DMN transformer
   * @return this
   */
  public DefaultDmnEngineConfiguration transformer(DmnTransformer transformer) {
    setTransformer(transformer);
    return this;
  }

}
