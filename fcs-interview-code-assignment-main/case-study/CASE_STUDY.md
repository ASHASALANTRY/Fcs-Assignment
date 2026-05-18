# Case Study Scenarios to discuss
## Scenario 1: Cost Allocation and Tracking
**Situation**: The company needs to track and allocate costs accurately across different Warehouses and Stores. The costs include labor, inventory, transportation, and overhead expenses.
**Task**: Discuss the challenges in accurately tracking and allocating costs in a fulfillment environment. Think about what are important considerations for this, what are previous experiences that you have you could related to this problem and elaborate some questions and considerations
**Questions you may have and considerations:**


### Questions I Would Ask:

**1. What's the primary purpose of cost tracking?**
- Is it for internal optimization (operational decisions)?
- For financial reporting (accounting compliance)?
- For pricing decisions (product profitability)?
- This determines the required accuracy and granularity.

**2. How are costs currently tracked, if at all?**
- Are there existing financial systems?
- What data is already captured?
- What are the known pain points?

**3. What level of granularity is needed?**
- Per warehouse? Per store? Per product? Per order?
- Real-time or periodic (monthly/quarterly)?
- Different needs have different technical requirements.

**4. Are there regulatory or compliance requirements?**
- Tax implications across different locations?
- Financial reporting standards?
- This affects how we must track and report costs.

**5. What's the volume and complexity?**
- How many warehouses, stores, products, transactions?
- Scale affects technical approach and cost of implementation.

### Key Considerations:

**Data Accuracy and Sources:**
Labor costs come from HR systems (payroll), inventory costs come from procurement/ERP systems, transportation costs from logistics systems, and overhead costs from facilities management. The challenge is that these systems may not talk to each other easily, requiring integration effort to get a complete cost picture.

**Shared Resources Problem:**
One warehouse serves multiple stores - how to split costs fairly? One store receives from multiple warehouses - same challenge. Overhead costs (rent, utilities) are shared across all operations. We need clear allocation rules that are defensible and consistent across the organization.

**Dynamic Nature of Operations:**
Inventory levels change constantly (as reflected in the assignment's `stock` field). Warehouses can be archived/replaced, which is why preserving cost history matters. Seasonal variations are significant for IKEA (back to school, holidays). Our allocation methods must handle this dynamism without becoming overly complex.

**Time Dimension:**
When do we allocate costs - when incurred or when realized? For example, labor cost incurred in January, but inventory sold in March. This requires period-end adjustments and reconciliation processes.

### Related Experience & Thoughts:

In the warehouse assignment, I implemented business unit codes and archiving functionality. This relates directly to cost tracking because:

- Each warehouse has a unique `businessUnitCode` - a natural cost center identifier
- Archiving doesn't delete data - it preserves historical information which is important for cost trends and year-over-year comparisons
- Location constraints affect costs (max capacity influences operational efficiency)
- Stock levels tie directly to inventory carrying costs

The challenge I see is the **many-to-many relationships**: warehouses serve multiple stores, and stores receive from multiple warehouses. This creates allocation complexity that simple direct costing can't solve effectively. Activity-based costing or transaction-level tracking might be needed, but that increases system complexity and data volume significantly.

The right approach depends on the business objective - perfect accuracy is expensive to achieve and maintain. Sometimes "good enough" allocation rules (like volume-based or transaction-count-based) provide sufficient insights at much lower implementation and operational cost. The key is balancing precision with practicality based on the actual business decisions that will be made with this data.

---


## Scenario 2: Cost Optimization Strategies
**Situation**: The company wants to identify and implement cost optimization strategies for its fulfillment operations. The goal is to reduce overall costs without compromising service quality.
**Task**: Discuss potential cost optimization strategies for fulfillment operations and expected outcomes from that. How would you identify, prioritize and implement these strategies?
**Questions you may have and considerations:**

### Questions I Would Ask:

**1. What are the current major cost drivers in fulfillment operations?**
- Labor, transportation, inventory carrying costs, or overhead?
- Understanding where money is actually spent helps target optimization efforts effectively.

**2. Which service quality metrics are most critical to maintain?**
- Delivery speed? Order accuracy? Product condition? Customer satisfaction scores?
- Need to define "non-negotiable" quality standards before optimizing costs.

**3. What optimizations have been attempted previously?**
- What worked? What failed? Why?
- Learning from past attempts prevents repeating mistakes and builds on successes.

**4. What constraints exist for implementation?**
- Budget available for optimization investments?
- Timeline expectations?
- Organizational appetite for change?

**5. What data is currently available for analysis?**
- Cost tracking per warehouse/store?
- Service quality metrics?
- Operational efficiency metrics?
- Quality of data affects quality of decisions.

---

### How I Would Identify Opportunities:

**Data-Driven Analysis:**
I would start by analyzing where costs are concentrated. Using the warehouse system's data (capacity utilization, stock levels, location efficiency), identify which operations are most expensive and why. For example, if some warehouses consistently operate at 60% capacity while others run at 90%, the underutilized ones represent optimization opportunities through consolidation or right-sizing.

**Benchmark and Compare:**
Compare performance across similar units - why does one warehouse process orders at €12 per order while another costs €18? Root cause analysis of these differences reveals actionable insights. Also benchmark against industry standards where available.

**Categorize by Impact:**
Group opportunities into categories: operational efficiency (warehouse utilization, labor productivity), network optimization (location strategy, transportation), technology (automation, forecasting systems), and process improvements (quality, returns handling). This structured approach ensures comprehensive coverage.

---

### Prioritization Framework:

I would prioritize based on three dimensions:

**Impact:** Expected cost savings (high >10%, medium 5-10%, low <5%)

**Effort:** Implementation complexity and time (low: 1-3 months, medium: 3-6 months, high: 6+ months)

**Risk:** Potential impact on service quality (low: no impact, medium: manageable impact, high: significant risk)

**Priority 1 - Quick Wins:** High impact, low effort, low risk. Example: Consolidating underutilized warehouses where delivery coverage isn't compromised. These build momentum and fund larger initiatives.

**Priority 2 - Strategic Initiatives:** High impact, medium effort, low-medium risk. Example: Implementing better demand forecasting to optimize inventory levels. These require planning but deliver substantial value.

**Priority 3 - Avoid:** Low impact, high effort, or high risk initiatives that don't justify the investment or could harm service quality.

Additional consideration: Some optimizations enable others (e.g., better data systems enable better forecasting), so sequencing matters beyond just the priority matrix.

---

### Implementation Approach:

**Pilot First:**
Rather than implementing organization-wide immediately, start with a pilot in one region or warehouse. This reduces risk and allows learning. For example, if optimizing warehouse layouts, test in one location, measure the impact on both costs and service quality, refine the approach, then scale.

**Measure Rigorously:**
Define success metrics upfront: cost savings targets AND service quality metrics (delivery time, order accuracy, damage rates). Monitor both continuously. If service quality degrades, pause and adjust. The goal is optimization, not just cost-cutting.

**Change Management:**
Technical solutions are only part of success - people need to understand and support changes. Communicate why optimizations are needed, train staff on new processes, address concerns, and celebrate wins to build momentum.

**Continuous Improvement:**
Optimization isn't one-time. After implementing, continue monitoring, identify new opportunities, and iterate. Small continuous improvements often deliver more value than occasional large initiatives.

---

### Expected Outcomes:

**Quantified Cost Savings:**
For a fulfillment operation at Ingka's scale, realistic targets might be 5-10% cost reduction in first year through combined initiatives: warehouse consolidation (2-3%), inventory optimization (2-3%), process improvements (1-2%), transportation efficiency (1-2%). This balances ambition with achievability.

**Service Quality Maintained:**
Key metrics like on-time delivery (≥98%), order accuracy (≥99.5%), and customer satisfaction (≥4.5/5) should be maintained or improved. If any decline, that particular optimization needs revision.

**Secondary Benefits:**
Beyond direct cost savings: reduced environmental impact (fewer trucks, less waste), better working conditions (automation of repetitive tasks), increased flexibility to handle demand peaks, and improved data visibility for better decision-making.

**Risk Mitigation:**
Risks include service degradation, employee resistance, or optimizations not delivering expected savings. Mitigation strategies: pilot approach reduces implementation risk, continuous monitoring enables quick course correction, change management reduces organizational resistance.

---

### Connection to Assignment Experience:

In implementing the warehouse system, several cost optimization concepts became clear:

**Capacity Utilization:** The `capacity` vs `stock` relationship directly impacts costs. A warehouse at 60% utilization carries the same overhead costs as one at 90%, making the latter much more cost-efficient. This validates why warehouse consolidation can be a powerful optimization strategy.

**Location Efficiency:** The location constraints (`maxNumberOfWarehouses`, `maxCapacity`) show that not all locations are equal. Some have natural limits. Cost optimization must work within these constraints - you can't just "add more capacity" everywhere.

**Historical Tracking:** The archive pattern (preserving warehouse history without deletion) is crucial for measuring optimization success. You need "before" data to prove "after" improvements. This applies to any cost optimization - measuring impact requires historical baseline data.

**Trade-off Thinking:** Similar to the architectural trade-offs discussed in the technical questions (Stores vs Warehouses patterns), cost optimization involves trade-offs. Lower cost often means trade-offs in flexibility, speed, or redundancy. The right answer depends on business priorities, not abstract "best practices."

The key lesson: optimization isn't about finding the theoretical minimum cost - it's about finding the right balance between cost, quality, and constraints in your specific context.

---

## Scenario 3: Integration with Financial Systems
**Situation**: The Cost Control Tool needs to integrate with existing financial systems to ensure accurate and timely cost data. The integration should support real-time data synchronization and reporting.
**Task**: Discuss the importance of integrating the Cost Control Tool with financial systems. What benefits the company would have from that and how would you ensure seamless integration and data synchronization?
**Questions you may have and considerations:**

### Questions I Would Ask:

**1. What financial systems currently exist and what data do they contain?**
- ERP system (SAP, Oracle, Microsoft Dynamics)?
- Legacy systems?
- What cost data already exists vs needs to be generated?
- Understanding current state informs integration approach.

**2. What are the critical business decisions that depend on this data?**
- Operational decisions (real-time requirements)?
- Strategic decisions (monthly/quarterly acceptable)?
- This determines real-time vs batch integration needs.

**3. What is the acceptable data latency for different use cases?**
- Real-time (seconds): Which decisions need this?
- Near real-time (minutes/hours): Which decisions need this?
- Batch (daily/monthly): Which decisions can tolerate this?
- Different requirements = different integration patterns.

**4. What happens when integration fails or data is inconsistent?**
- Fallback procedures?
- Manual reconciliation process?
- Tolerance for temporary inconsistency?
- Need to plan for failure scenarios upfront.

**5. Who owns data quality and reconciliation?**
- IT team? Finance team? Operations team?
- What are the SLAs (Service Level Agreements)?
- Clear ownership prevents accountability gaps.

---

### Importance of Financial Integration:

**Business Value:**
The integration eliminates manual data reconciliation, which is time-consuming and error-prone at scale. For Ingka with hundreds of warehouses and stores, manual processes are unsustainable. Integration provides a "single source of truth" for cost data, enabling accurate and timely financial reporting.

**Decision-Making Enablement:**
Real-time or near real-time cost visibility enables proactive management. Instead of discovering cost overruns at month-end, managers can course-correct immediately. This is especially critical for operational decisions like warehouse capacity planning and resource allocation.

**Compliance and Auditability:**
Integrated systems provide clear data lineage - tracking where each cost came from and how it was calculated. This is essential for financial audits, regulatory compliance, and internal controls. Manual processes lack this traceability.

**Scalability:**
As the business grows, manual processes don't scale. Integration effort is roughly the same whether handling 10 warehouses or 100. This "build once, scale indefinitely" characteristic makes integration a strategic investment, not just a technical improvement.

---

### How I Would Ensure Seamless Integration:

**Start with Data Mapping:**
Identify what data exists in each system and what needs to flow where. For example, the warehouse system (from the assignment) provides businessUnitCode, location, capacity, stock - this maps to cost center data in financial systems. Understanding these mappings prevents integration surprises.

**Define Integration Patterns:**
Not everything needs real-time integration. Critical operational data (stock levels, capacity) might need real-time updates. Financial aggregations (monthly cost totals) can be batch processed. I would categorize data flows by urgency and choose appropriate patterns - event-driven for real-time, scheduled batch for periodic updates.

**Implement Data Validation:**
Integration without validation creates problems. I would implement validation rules at the integration layer: check data completeness (are all warehouses present?), consistency (do numbers add up?), and reasonableness (is cost within expected range?). Flag anomalies for human review rather than blindly accepting all data.

**Build Reconciliation Processes:**
Even with good integration, discrepancies happen. I would implement daily reconciliation - compare what the warehouse system thinks costs are versus what the financial system recorded. Automated alerts when differences exceed threshold, with clear ownership for resolution.

**Plan for Failure:**
Integration will fail sometimes - network issues, system downtime, bad data. I would design for resilience: message queuing (don't lose data during outages), retry mechanisms (automatic recovery), and fallback procedures (manual processes when automation fails). Monitor integration health continuously and alert proactively.

**Incremental Rollout:**
Rather than integrating all systems at once, start with one critical data flow (e.g., warehouse cost center creation). Validate it works correctly, then add more integrations iteratively. This "crawl, walk, run" approach reduces risk and allows learning.

---

### Expected Benefits:

**Time Efficiency:**
Manual reconciliation currently takes days; integrated systems provide instant access. For month-end close processes, this could reduce timeline from 2 weeks to 2 days - enabling faster business decisions and reducing accounting team workload.

**Cost Accuracy:**
Eliminating manual data entry and reconciliation reduces errors. Even 1-2% improvement in cost accuracy, at Ingka's scale (€4B fulfillment costs), means tens of millions in better decisions (closing right warehouses, pricing correctly, allocating resources optimally).

**Real-Time Visibility:**
Managers see costs as they occur, not weeks later. This enables proactive management - "We're trending 10% over budget in Week 2, let's investigate" versus "We were 10% over budget last month, too late to fix."

**Audit and Compliance:**
Integrated systems create automatic audit trails. When auditors ask "how did you calculate this cost?", the system can show the complete data lineage. This reduces audit preparation time and increases confidence in financial reporting.

---

### Connection to Assignment Experience:

In implementing the warehouse system, several integration concepts became evident:

**Business Unit Code as Integration Key:** The `businessUnitCode` acts as the primary identifier linking warehouse operations to financial data. In an integrated system, this code would be the "foreign key" connecting operational events (orders processed, stock movements) to financial allocations.

**Lifecycle Events Requiring Synchronization:** The archive functionality demonstrates that warehouse lifecycle extends beyond creation. When a warehouse is archived, the financial system needs notification to stop allocating future costs but preserve historical data. This is event-driven integration in practice.

**Data Consistency Requirements:** The validation rules (stock ≤ capacity, location constraints) show that data quality matters. Financial integration amplifies this - bad data in warehouse system becomes bad data in financial reports. Integration must include validation at boundaries.

**Historical Data Preservation:** The archive pattern (not deleting, just marking archived) is crucial for financial integration. Finance teams need historical cost data for trend analysis, audits, and year-over-year comparisons. Integration can't just sync current state; it must handle historical data correctly.

The key lesson: integration is not just technical plumbing. It requires understanding business processes, data relationships, and failure scenarios. Good integration is invisible when working correctly but handles problems gracefully when things go wrong.

---

## Scenario 4: Budgeting and Forecasting
**Situation**: The company needs to develop budgeting and forecasting capabilities for its fulfillment operations. The goal is to predict future costs and allocate resources effectively.
**Task**: Discuss the importance of budgeting and forecasting in fulfillment operations and what would you take into account designing a system to support accurate budgeting and forecasting?
**Questions you may have and considerations:**

### Questions I Would Ask:

**1. What is the primary purpose of budgeting and forecasting?**
- Resource planning (hiring, capacity)?
- Financial reporting (board, investors)?
- Operational decisions (daily management)?
- Purpose determines accuracy requirements and update frequency.

**2. What historical data is available and how reliable is it?**
- Years of data? Months?
- Data quality issues?
- System changes that make historical data non-comparable?
- Forecast accuracy depends on data quality and quantity.

**3. What are the key drivers of fulfillment costs?**
- Volume (orders processed)?
- Complexity (product types, delivery methods)?
- External factors (fuel prices, labor market)?
- Understanding drivers enables better forecasting models.

**4. How much variability exists in the business?**
- Seasonal patterns (Q4 peak)?
- Predictable events (promotional campaigns)?
- Unpredictable events (supply chain disruptions)?
- Variability affects forecast uncertainty and planning buffers.

**5. Who will use forecasts and for what decisions?**
- Operations (staffing decisions)?
- Finance (budget management)?
- Strategy (expansion decisions)?
- Usage determines required granularity and update frequency.

---

### Importance of Budgeting and Forecasting:

**Proactive Resource Management:**
Budgeting and forecasting transform operations from reactive to proactive. Instead of responding to capacity shortages or cost overruns after they occur, organizations can anticipate needs and prepare accordingly. For fulfillment operations with seasonal peaks (like IKEA's Q4 holiday season), this is critical - you can't hire and train 100 workers overnight when volume suddenly spikes.

**Capital Allocation Decisions:**
Major investments (new warehouses, automation equipment) require multi-year planning and significant capital. Forecasting future volumes and costs justifies these investments and determines optimal timing. Without forecasting, organizations either invest too early (wasting capital) or too late (missing opportunities, service failures).

**Performance Management:**
Budgets provide targets and accountability. Forecasts enable variance analysis - "we planned X, we're trending toward Y because of Z" - allowing course correction. This closed-loop planning process (plan → execute → measure → adjust) drives continuous improvement.

**Stakeholder Communication:**
Board members, investors, and executive leadership need forward visibility into costs and performance. Accurate forecasts enable credible commitments and manage expectations. Unexpected variances create credibility problems and operational pressure.

---

### What I Would Take Into Account Designing the System:

**Historical Data Foundation:**
The system needs comprehensive historical data as the foundation for forecasting. This includes operational metrics (volume, capacity utilization, stock levels) and financial data (costs by category and warehouse). From the warehouse assignment, the archive pattern is crucial here - preserving historical warehouse data even after replacement enables trend analysis and model training. I would ensure the system captures at least 2-3 years of history, ideally more, with consistent definitions across time periods.

**Seasonality and Patterns:**
Fulfillment operations have strong seasonal patterns. The system must decompose historical data into components: baseline trends (long-term growth), seasonal patterns (Q4 peak), and irregular events (promotional campaigns, disruptions). Standard time series techniques can identify these patterns automatically, but domain knowledge helps - knowing Black Friday causes spikes enables better forecasting than purely statistical approaches.

**External Factors Integration:**
Not all cost drivers are internal. Fuel prices affect transportation costs, labor market conditions affect wages, and economic conditions affect volume. The system should integrate relevant external data sources (economic indicators, commodity prices, competitor actions) and model their relationships with fulfillment costs. This enables "what-if" analysis: "if fuel prices increase 20%, our costs increase X%."

**Multiple Scenarios and Confidence Intervals:**
Single-point forecasts ("costs will be €4.2B") are rarely useful because they're rarely correct. The system should support scenario planning (optimistic, base, pessimistic cases) and provide confidence intervals ("costs will be €4.0-4.4B with 90% confidence"). This acknowledges forecast uncertainty and enables risk management - plan for base case, prepare contingencies for pessimistic case.

**Granularity and Aggregation:**
Different decisions need different granularity. Strategic decisions (should we expand?) need high-level annual forecasts. Operational decisions (how many workers this week?) need detailed short-term forecasts. The system should support multiple levels: by warehouse, by region, company-wide; by week, by month, by quarter, by year. Forecasts should aggregate correctly - sum of warehouse forecasts = company total.

**Human Judgment and Override:**
Statistical models don't know about planned changes (new store openings, warehouse closures, strategic initiatives). The system must allow humans to incorporate business knowledge: "model says 5% growth, but we're opening 20 new stores, so adjust to 8% growth." This human-in-the-loop approach combines statistical rigor with business judgment.

**Continuous Learning and Adaptation:**
Forecasts should be evaluated against actuals regularly - "we forecasted X, actual was Y, error was Z." The system should track forecast accuracy, identify systematic biases, and adapt models accordingly. This feedback loop drives continuous improvement. Rolling forecasts (updating monthly/quarterly) ensure forecasts stay current rather than becoming outdated annual exercises.

---

### Connection to Assignment Experience:

In implementing the warehouse system, several forecasting foundations became clear:

**Capacity Planning Needs:** The `capacity` field represents a constraint that forecasting must respect. If forecasts show volume growing beyond capacity, this triggers investment decisions. The validation rule (stock ≤ capacity) is essentially saying "we can't fulfill what we can't store" - forecasting must account for these physical constraints.

**Location-Based Constraints:** The location's `maxNumberOfWarehouses` and `maxCapacity` show that expansion isn't unlimited. Forecasting must consider these constraints when predicting future needs. You can't just say "we need 20% more capacity" if the location only allows 2 warehouses and both are at maximum.

**Historical Data Preservation:** The archive pattern is crucial for forecasting. When warehouse MWH.001 is replaced, its cost and volume history must be preserved to understand long-term trends. Without this historical data, forecasting becomes guesswork. The `archivedAt` timestamp enables "before/after" analysis - was the replacement effective?

**Operational Metrics as Forecast Inputs:** The system tracks stock levels, which reflect actual demand patterns. Historical stock data shows seasonal patterns (high in Q4, low in Q1-Q2) that are essential inputs to forecasting models. Operational data drives financial forecasts.

The key lesson: forecasting isn't a separate activity from operations. The operational system (warehouse management) generates the data that enables forecasting, and forecasting insights drive operational decisions (when to expand, how to staff). They're interconnected, not standalone functions.

---


## Scenario 5: Cost Control in Warehouse Replacement
**Situation**: The company is planning to replace an existing Warehouse with a new one. The new Warehouse will reuse the Business Unit Code of the old Warehouse. The old Warehouse will be archived, but its cost history must be preserved.
**Task**: Discuss the cost control aspects of replacing a Warehouse. Why is it important to preserve cost history and how this relates to keeping the new Warehouse operation within budget?
**Questions you may have and considerations:**

### Questions I Would Ask:

**1. What is the primary driver for replacing this warehouse?**
- Cost reduction? Capacity increase? Location optimization?
- Understanding the goal determines what cost metrics matter most.

**2. What cost data is currently captured for the existing warehouse?**
- Detailed cost breakdowns by category (labor, rent, utilities)?
- Historical trends over past years?
- Data quality and availability affects analysis capability.

**3. What are the expected outcomes and how will success be measured?**
- Target cost per order? Total cost reduction? Capacity increase?
- Clear success criteria enable proper cost control.

**4. How will the transition be managed and tracked?**
- Overlap period? Immediate cutover?
- Transition costs need separate tracking from ongoing operations.

**5. What lessons were learned from previous warehouse replacements?**
- What worked? What didn't?
- Institutional knowledge improves future planning.

---

### Cost Control Aspects of Warehouse Replacement:

**Investment Justification:**
Warehouse replacement requires significant capital investment (€5-20M for building, equipment, automation). The business case depends entirely on demonstrating cost reduction or capacity increase. Historical cost data provides the baseline: "Current warehouse costs €18/order. New warehouse targets €14/order. With 1M annual orders, that's €4M annual savings, justifying the €10M investment with 2.5 year payback." Without historical costs, you can't build a credible business case.

**Target Setting and Accountability:**
Historical performance establishes realistic targets for the new warehouse. If the old warehouse cost €18/order, targeting €10/order might be unrealistic, while €14/order (22% improvement) is achievable with modern design and automation. These targets create accountability - stakeholders can track whether the new warehouse delivers promised savings. Targets anchored in historical reality are more credible than arbitrary goals.

**Performance Measurement:**
After replacement, cost control requires comparing new warehouse performance against the old warehouse baseline. Month-by-month comparison shows whether savings are materializing: "Old warehouse June: €18/order. New warehouse June: €15/order. Savings: €3/order, 83% of target achieved." This ongoing measurement enables course correction if costs are higher than expected.

**Variance Analysis:**
When new warehouse costs deviate from targets, historical data enables root cause analysis. If costs are €17/order instead of €14/order, compare to historical patterns: Is volume different? Are seasonal patterns affecting comparison? Are specific cost categories (labor, maintenance) higher? Historical decomposition identifies where to focus improvement efforts.

---

### Why Preserving Cost History is Critical:

**Baseline for Comparison:**
The fundamental reason to preserve history is to establish a baseline. You can't measure improvement without knowing the starting point. "New warehouse costs €14/order" means nothing without context. "Old warehouse cost €18/order, new costs €14/order, 22% improvement" provides actionable insight. The archived warehouse's cost history is this baseline.

**Apples-to-Apples Comparison:**
Seasonal patterns make month-to-month comparisons tricky. December (peak season) always costs more per order than February (low season). Historical data enables proper comparison: "New warehouse December €15/order vs old warehouse December €19/order" is meaningful. "New warehouse December €15/order vs old warehouse February €16/order" is misleading. Preserved history ensures fair comparisons.

**Trend Analysis:**
Cost trends emerge over time. Is the new warehouse improving month-over-month? Is it following historical seasonal patterns or behaving differently? Three months of data shows direction: "Month 1: €16/order, Month 2: €15/order, Month 3: €14/order - trending toward target." Historical patterns provide context: "Historical trend showed 2% monthly improvement in first year, new warehouse matching this pattern."

**Learning and Continuous Improvement:**
Organizations that preserve historical data build institutional knowledge. "In 2024 replacement, we targeted 30% cost reduction but achieved 15%. In 2026 replacement, we set more realistic 20% target and achieved 18%." This learning loop improves future decisions. Without historical data, each replacement starts from zero knowledge.

---

### How This Relates to Budget Management:

**Budget Development:**
The new warehouse's first-year budget is based on historical costs with adjustments for improvements. If old warehouse ran at €18M/year, new warehouse might budget €15M/year (targeting 17% reduction). This budget is grounded in reality, not guesswork. Historical data provides components: labor typically 40% of costs, if new warehouse automates picking, budget 30% reduction in that component.

**Variance Monitoring:**
During the year, actual costs are compared to budget monthly. If March actual is €1.3M vs €1.25M budget, investigate immediately. Historical data helps: "March is historically 8% of annual costs, we're at 8.7%, slightly high. Check if volume spike or cost overrun." Early detection enables correction before year-end surprises.

**Forecasting Adjustments:**
If new warehouse is tracking above or below budget after a few months, update forecasts. "We budgeted €15M annual but tracking €16M based on Q1 actuals. Historical Q1 represents 22% of annual costs, so forecasting €16M × (100/22) for full year." Historical seasonal patterns improve forecast accuracy.

**Multi-Year Planning:**
Historical data from the old warehouse informs multi-year expectations for the new warehouse. "Old warehouse costs increased 3% annually (labor inflation, maintenance). Budget new warehouse with 2% annual increase (better equipment, lower maintenance)." Long-term budget planning requires understanding historical cost drivers and trends.

---

### Connection to Assignment Experience:

The archive functionality I implemented directly enables this cost control process:

**Archive Pattern Preserves History:** The `archivedAt` timestamp marks the warehouse as inactive without deleting it. All historical data (capacity, stock, costs) remains in the database. This preserved data is the baseline for comparing new warehouse performance.

**Business Unit Code Continuity:** Reusing the businessUnitCode for the new warehouse creates seamless continuity in financial systems. The cost center "MWH.001" shows an unbroken history - old warehouse performance, transition period, new warehouse performance. This continuity simplifies reporting and trend analysis.

**Query Flexibility:** The system can query both active warehouses (for operations) and archived warehouses (for analysis). `WHERE archivedAt IS NULL` returns active warehouses. `WHERE businessUnitCode = 'MWH.001' ORDER BY archivedAt` returns replacement history. This dual-purpose design supports both operational and analytical needs.

**Timestamp-Based Analysis:** The `createdAt` and `archivedAt` timestamps define operational periods precisely. "Old warehouse operated Jan 2020 - Dec 2024, new warehouse Jan 2025 - present." Cost analysis can aggregate costs by these periods for clean before/after comparison.

The key lesson: the archive pattern isn't just about data retention - it's about enabling business analysis. The technical implementation (soft delete with timestamp) directly supports the business need (cost control through historical comparison). Good technical design anticipates business requirements.

---


## Instructions for Candidates
Before starting the case study, read the [BRIEFING.md](BRIEFING.md) to quickly understand the domain, entities, business rules, and other relevant details.
**Analyze the Scenarios**: Carefully analyze each scenario and consider the tasks provided. To make informed decisions about the project's scope and ensure valuable outcomes, what key information would you seek to gather before defining the boundaries of the work? Your goal is to bridge technical aspects with business value, bringing a high level discussion; no need to deep dive.