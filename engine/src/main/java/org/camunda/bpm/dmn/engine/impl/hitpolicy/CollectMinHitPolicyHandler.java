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

package org.camunda.bpm.dmn.engine.impl.hitpolicy;

import java.util.Collections;
import java.util.List;

import org.camunda.bpm.model.dmn.BuiltinAggregator;

public class CollectMinHitPolicyHandler extends AbstractCollectNumberHitPolicyHandler {

  protected BuiltinAggregator getAggregator() {
    return BuiltinAggregator.MIN;
  }

  @Override
  protected Integer aggregateIntegerValues(List<Integer> intValues) {
    return Collections.min(intValues);
  }

  @Override
  protected Long aggregateLongValues(List<Long> longValues) {
    return Collections.min(longValues);
  }

  @Override
  protected Double aggregateDoubleValues(List<Double> doubleValues) {
    return Collections.min(doubleValues);
  }

  @Override
  public String toString() {
    return "CollectMinHitPolicyHandler{}";
  }

}
