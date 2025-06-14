/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.portfolio.savings.data;

import static org.apache.fineract.portfolio.savings.DepositsApiConstants.adjustAdvanceTowardsFuturePaymentsParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.allowWithdrawalParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.annualInterestRateParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.depositAmountParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.depositPeriodFrequencyIdParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.depositPeriodParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.inMultiplesOfDepositTermParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.inMultiplesOfDepositTermTypeIdParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.interestCompoundingPeriodInMonthsParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.interestPostingPeriodInMonthsParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.isCalendarInheritedParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.isMandatoryDepositParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.linkedAccountParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.mandatoryRecommendedDepositAmountParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.maturityInstructionIdParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.maxDepositTermParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.maxDepositTermTypeIdParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.minDepositTermParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.minDepositTermTypeIdParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.preClosurePenalApplicableParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.preClosurePenalInterestOnTypeIdParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.preClosurePenalInterestParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.principalAmountParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.recurringFrequencyParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.recurringFrequencyTypeParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.tenureInMonthsParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.transferInterestToSavingsParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.transferToSavingsIdParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.accountNoParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.amountParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.chargeIdParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.chargesParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.clientIdParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.externalIdParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.feeIntervalParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.feeOnMonthDayParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.fieldOfficerIdParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.groupIdParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.interestCalculationDaysInYearTypeParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.interestCalculationTypeParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.interestCompoundingPeriodTypeParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.interestPostingPeriodTypeParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.lockinPeriodFrequencyParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.lockinPeriodFrequencyTypeParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.minRequiredOpeningBalanceParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.nominalAnnualInterestRateParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.productIdParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.submittedOnDateParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.withHoldTaxParamName;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.fineract.infrastructure.core.data.ApiParameterError;
import org.apache.fineract.infrastructure.core.data.DataValidatorBuilder;
import org.apache.fineract.infrastructure.core.exception.InvalidJsonException;
import org.apache.fineract.infrastructure.core.exception.PlatformApiDataValidationException;
import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.apache.fineract.portfolio.savings.DepositAccountOnClosureType;
import org.apache.fineract.portfolio.savings.DepositAccountType;
import org.apache.fineract.portfolio.savings.DepositsApiConstants;
import org.apache.fineract.portfolio.savings.PreClosurePenalInterestOnType;
import org.apache.fineract.portfolio.savings.SavingsCompoundingInterestPeriodType;
import org.apache.fineract.portfolio.savings.SavingsInterestCalculationDaysInYearType;
import org.apache.fineract.portfolio.savings.SavingsInterestCalculationType;
import org.apache.fineract.portfolio.savings.SavingsPeriodFrequencyType;
import org.apache.fineract.portfolio.savings.SavingsPostingInterestPeriodType;
import org.apache.fineract.portfolio.savings.domain.SavingsAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DepositAccountDataValidator {

    private final FromJsonHelper fromApiJsonHelper;
    private final DepositProductDataValidator productDataValidator;

    @Autowired
    public DepositAccountDataValidator(final FromJsonHelper fromApiJsonHelper, final DepositProductDataValidator productDataValidator) {
        this.fromApiJsonHelper = fromApiJsonHelper;
        this.productDataValidator = productDataValidator;
    }

    public void validateFixedDepositForSubmit(final String json) {
        if (StringUtils.isBlank(json)) {
            throw new InvalidJsonException();
        }

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json,
                DepositsApiConstants.FIXED_DEPOSIT_ACCOUNT_REQUEST_DATA_PARAMETERS);

        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors)
                .resource(DepositsApiConstants.FIXED_DEPOSIT_ACCOUNT_RESOURCE_NAME);
        final JsonElement element = this.fromApiJsonHelper.parse(json);

        validateDepositDetailsForSubmit(element, baseDataValidator);
        validatePreClosureDetailForSubmit(element, baseDataValidator);
        validateDepositTermDeatilForSubmit(element, baseDataValidator, DepositAccountType.FIXED_DEPOSIT);
        validateSavingsCharges(element, baseDataValidator);
        validateWithHoldTax(element, baseDataValidator);

        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }

    public void validateFixedDepositForUpdate(final String json) {
        if (StringUtils.isBlank(json)) {
            throw new InvalidJsonException();
        }

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json,
                DepositsApiConstants.FIXED_DEPOSIT_ACCOUNT_REQUEST_DATA_PARAMETERS);

        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors)
                .resource(DepositsApiConstants.FIXED_DEPOSIT_ACCOUNT_RESOURCE_NAME);
        final JsonElement element = this.fromApiJsonHelper.parse(json);

        validateDepositDetailsForUpdate(element, baseDataValidator);
        validatePreClosureDetailForUpdate(element, baseDataValidator);
        validateDepositTermDeatilForUpdate(element, baseDataValidator, DepositAccountType.FIXED_DEPOSIT);
        // validateSavingsCharges(element, baseDataValidator);
        validateWithHoldTax(element, baseDataValidator);

        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }

    public void validateRecurringDepositForSubmit(final String json) {
        if (StringUtils.isBlank(json)) {
            throw new InvalidJsonException();
        }

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json,
                DepositsApiConstants.RECURRING_DEPOSIT_ACCOUNT_REQUEST_DATA_PARAMETERS);

        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors)
                .resource(DepositsApiConstants.RECURRING_DEPOSIT_ACCOUNT_RESOURCE_NAME);
        final JsonElement element = this.fromApiJsonHelper.parse(json);

        validateDepositDetailsForSubmit(element, baseDataValidator);
        validatePreClosureDetailForSubmit(element, baseDataValidator);
        validateDepositTermDeatilForSubmit(element, baseDataValidator, DepositAccountType.RECURRING_DEPOSIT);
        validateRecurringDetailForSubmit(element, baseDataValidator);
        validateSavingsCharges(element, baseDataValidator);
        validateWithHoldTax(element, baseDataValidator);

        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }

    public void validateRecurringDepositForUpdate(final String json) {
        if (StringUtils.isBlank(json)) {
            throw new InvalidJsonException();
        }

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json,
                DepositsApiConstants.RECURRING_DEPOSIT_ACCOUNT_REQUEST_DATA_PARAMETERS);

        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors)
                .resource(DepositsApiConstants.RECURRING_DEPOSIT_ACCOUNT_RESOURCE_NAME);
        final JsonElement element = this.fromApiJsonHelper.parse(json);

        validateDepositDetailsForUpdate(element, baseDataValidator);
        validatePreClosureDetailForUpdate(element, baseDataValidator);
        validateDepositTermDeatilForUpdate(element, baseDataValidator, DepositAccountType.RECURRING_DEPOSIT);
        validateRecurringDetailForUpdate(element, baseDataValidator);
        // validateSavingsCharges(element, baseDataValidator);
        validateWithHoldTax(element, baseDataValidator);

        throwExceptionIfValidationWarningsExist(dataValidationErrors);

    }

    public void validateFixedDepositForInterestCalculation(final String json) {
        if (StringUtils.isBlank(json)) {
            throw new InvalidJsonException();
        }
        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json,
                DepositsApiConstants.FIXED_DEPOSIT_ACCOUNT_INTEREST_CALCULATION_PARAMETERS);
        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors)
                .resource(DepositsApiConstants.FIXED_DEPOSIT_ACCOUNT_RESOURCE_NAME);
        final JsonElement element = this.fromApiJsonHelper.parse(json);

        validateForInterestCalc(element, baseDataValidator);
        throwExceptionIfValidationWarningsExist(dataValidationErrors);

    }

    private void validateDepositDetailsForSubmit(final JsonElement element, final DataValidatorBuilder baseDataValidator) {

        final Long clientId = this.fromApiJsonHelper.extractLongNamed(clientIdParamName, element);
        if (clientId != null) {
            baseDataValidator.reset().parameter(clientIdParamName).value(clientId).longGreaterThanZero();
        }

        final Long groupId = this.fromApiJsonHelper.extractLongNamed(groupIdParamName, element);
        if (groupId != null) {
            baseDataValidator.reset().parameter(groupIdParamName).value(groupId).longGreaterThanZero();
        }

        if (clientId == null && groupId == null) {
            baseDataValidator.reset().parameter(clientIdParamName).value(clientId).notNull().integerGreaterThanZero();
        }

        final Long productId = this.fromApiJsonHelper.extractLongNamed(productIdParamName, element);
        baseDataValidator.reset().parameter(productIdParamName).value(productId).notNull().integerGreaterThanZero();

        if (this.fromApiJsonHelper.parameterExists(fieldOfficerIdParamName, element)) {
            final Long fieldOfficerId = this.fromApiJsonHelper.extractLongNamed(fieldOfficerIdParamName, element);
            baseDataValidator.reset().parameter(fieldOfficerIdParamName).value(fieldOfficerId).ignoreIfNull().integerGreaterThanZero();
        }

        final LocalDate submittedOnDate = this.fromApiJsonHelper.extractLocalDateNamed(submittedOnDateParamName, element);
        baseDataValidator.reset().parameter(submittedOnDateParamName).value(submittedOnDate).notNull();

        if (this.fromApiJsonHelper.parameterExists(accountNoParamName, element)) {
            final String accountNo = this.fromApiJsonHelper.extractStringNamed(accountNoParamName, element);
            baseDataValidator.reset().parameter(accountNoParamName).value(accountNo).notBlank().notExceedingLengthOf(20);
        }

        if (this.fromApiJsonHelper.parameterExists(externalIdParamName, element)) {
            final String externalId = this.fromApiJsonHelper.extractStringNamed(externalIdParamName, element);
            baseDataValidator.reset().parameter(externalIdParamName).value(externalId).notExceedingLengthOf(100);
        }

        if (this.fromApiJsonHelper.parameterExists(nominalAnnualInterestRateParamName, element)) {
            final BigDecimal interestRate = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed(nominalAnnualInterestRateParamName,
                    element);
            baseDataValidator.reset().parameter(nominalAnnualInterestRateParamName).value(interestRate).notNull().zeroOrPositiveAmount();
        }

        if (this.fromApiJsonHelper.parameterExists(interestCompoundingPeriodTypeParamName, element)) {
            final Integer interestCompoundingPeriodType = this.fromApiJsonHelper
                    .extractIntegerSansLocaleNamed(interestCompoundingPeriodTypeParamName, element);
            baseDataValidator.reset().parameter(interestCompoundingPeriodTypeParamName).value(interestCompoundingPeriodType).notNull()
                    .isOneOfTheseValues(SavingsCompoundingInterestPeriodType.integerValues());
        }

        if (this.fromApiJsonHelper.parameterExists(interestPostingPeriodTypeParamName, element)) {
            final Integer interestPostingPeriodType = this.fromApiJsonHelper
                    .extractIntegerSansLocaleNamed(interestPostingPeriodTypeParamName, element);
            baseDataValidator.reset().parameter(interestPostingPeriodTypeParamName).value(interestPostingPeriodType).notNull()
                    .isOneOfTheseValues(SavingsPostingInterestPeriodType.integerValues());
        }

        if (this.fromApiJsonHelper.parameterExists(interestCalculationTypeParamName, element)) {
            final Integer interestCalculationType = this.fromApiJsonHelper.extractIntegerSansLocaleNamed(interestCalculationTypeParamName,
                    element);
            baseDataValidator.reset().parameter(interestCalculationTypeParamName).value(interestCalculationType).notNull()
                    .isOneOfTheseValues(SavingsInterestCalculationType.integerValues());
        }

        if (this.fromApiJsonHelper.parameterExists(interestCalculationDaysInYearTypeParamName, element)) {
            final Integer interestCalculationDaysInYearType = this.fromApiJsonHelper
                    .extractIntegerSansLocaleNamed(interestCalculationDaysInYearTypeParamName, element);
            baseDataValidator.reset().parameter(interestCalculationDaysInYearTypeParamName).value(interestCalculationDaysInYearType)
                    .notNull().isOneOfTheseValues(SavingsInterestCalculationDaysInYearType.integerValues());
        }

        if (this.fromApiJsonHelper.parameterExists(minRequiredOpeningBalanceParamName, element)) {
            final BigDecimal minOpeningBalance = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed(minRequiredOpeningBalanceParamName,
                    element);
            baseDataValidator.reset().parameter(minRequiredOpeningBalanceParamName).value(minOpeningBalance).zeroOrPositiveAmount();
        }

        boolean isLockinPeriodFrequencyValidated = false;
        boolean isLockinPeriodFrequencyTypeValidated = false;
        if (this.fromApiJsonHelper.parameterExists(lockinPeriodFrequencyParamName, element)) {
            isLockinPeriodFrequencyValidated = true;
            final Integer lockinPeriodFrequency = this.fromApiJsonHelper.extractIntegerWithLocaleNamed(lockinPeriodFrequencyParamName,
                    element);
            baseDataValidator.reset().parameter(lockinPeriodFrequencyParamName).value(lockinPeriodFrequency).ignoreIfNull();

            if (lockinPeriodFrequency != null) {
                isLockinPeriodFrequencyTypeValidated = true;
                final Integer lockinPeriodFrequencyType = this.fromApiJsonHelper
                        .extractIntegerSansLocaleNamed(lockinPeriodFrequencyTypeParamName, element);
                baseDataValidator.reset().parameter(lockinPeriodFrequencyTypeParamName).value(lockinPeriodFrequencyType).ignoreIfNull()
                        .inMinMaxRange(0, 3);
            }
        }

        if (!isLockinPeriodFrequencyTypeValidated && this.fromApiJsonHelper.parameterExists(lockinPeriodFrequencyTypeParamName, element)) {
            final Integer lockinPeriodFrequencyType = this.fromApiJsonHelper
                    .extractIntegerSansLocaleNamed(lockinPeriodFrequencyTypeParamName, element);
            baseDataValidator.reset().parameter(lockinPeriodFrequencyTypeParamName).value(lockinPeriodFrequencyType).inMinMaxRange(0, 3);

            if (lockinPeriodFrequencyType != null && !isLockinPeriodFrequencyValidated) {
                final Integer lockinPeriodFrequency = this.fromApiJsonHelper.extractIntegerWithLocaleNamed(lockinPeriodFrequencyParamName,
                        element);
                baseDataValidator.reset().parameter(lockinPeriodFrequencyParamName).value(lockinPeriodFrequency).ignoreIfNull()
                        .integerZeroOrGreater();
            }
        }

        boolean isLinkedAccRequired = false;
        if (fromApiJsonHelper.parameterExists(transferInterestToSavingsParamName, element)) {
            isLinkedAccRequired = fromApiJsonHelper.extractBooleanNamed(transferInterestToSavingsParamName, element);
        }

        final Long linkAccountId = this.fromApiJsonHelper.extractLongNamed(linkedAccountParamName, element);
        if (isLinkedAccRequired) {
            baseDataValidator.reset().parameter(linkedAccountParamName).value(linkAccountId).notNull().longGreaterThanZero();
        } else {
            baseDataValidator.reset().parameter(linkedAccountParamName).value(linkAccountId).ignoreIfNull().longGreaterThanZero();
        }

        if (this.fromApiJsonHelper.parameterExists(maturityInstructionIdParamName, element)) {
            final Integer depositRolloverId = this.fromApiJsonHelper.extractIntegerSansLocaleNamed(maturityInstructionIdParamName, element);
            baseDataValidator.reset().parameter(maturityInstructionIdParamName).value(depositRolloverId).notNull()
                    .isOneOfTheseValues(DepositAccountOnClosureType.integerValues());

            if (depositRolloverId.equals(DepositAccountOnClosureType.TRANSFER_TO_SAVINGS.getValue())) {
                final Long transferToSavingsId = this.fromApiJsonHelper.extractLongNamed(transferToSavingsIdParamName, element);
                baseDataValidator.reset().parameter(transferToSavingsIdParamName).value(transferToSavingsId).notNull()
                        .longGreaterThanZero();
            }
        }
    }

    private void validateDepositDetailsForUpdate(final JsonElement element, final DataValidatorBuilder baseDataValidator) {
        Long clientId = null;
        if (this.fromApiJsonHelper.parameterExists(clientIdParamName, element)) {
            clientId = this.fromApiJsonHelper.extractLongNamed(clientIdParamName, element);
            baseDataValidator.reset().parameter(clientIdParamName).value(clientId).ignoreIfNull().longGreaterThanZero();

            Long groupId = null;
            if (this.fromApiJsonHelper.parameterExists(productIdParamName, element)) {
                groupId = this.fromApiJsonHelper.extractLongNamed(groupIdParamName, element);
                baseDataValidator.reset().parameter(groupIdParamName).value(groupId).ignoreIfNull().longGreaterThanZero();
            }

            if (clientId == null && groupId == null) {
                // either clientId or groupId must exists if param passed for
                // update.
                baseDataValidator.reset().parameter(clientIdParamName).value(clientId).notNull().integerGreaterThanZero();
            }
        }

        Long groupId = null;
        if (this.fromApiJsonHelper.parameterExists(groupIdParamName, element)) {
            groupId = this.fromApiJsonHelper.extractLongNamed(groupIdParamName, element);
            baseDataValidator.reset().parameter(groupIdParamName).value(groupId).ignoreIfNull().longGreaterThanZero();

            if (this.fromApiJsonHelper.parameterExists(clientIdParamName, element)) {
                clientId = this.fromApiJsonHelper.extractLongNamed(clientIdParamName, element);
                baseDataValidator.reset().parameter(clientIdParamName).value(clientId).ignoreIfNull().longGreaterThanZero();
            }

            if (clientId == null && groupId == null) {
                // either clientId or groupId must exists if param passed for
                // update.
                baseDataValidator.reset().parameter(clientIdParamName).value(clientId).notNull().integerGreaterThanZero();
            }
        }

        if (this.fromApiJsonHelper.parameterExists(productIdParamName, element)) {
            final Long productId = this.fromApiJsonHelper.extractLongNamed(productIdParamName, element);
            baseDataValidator.reset().parameter(productIdParamName).value(productId).notNull().integerGreaterThanZero();
        }

        if (this.fromApiJsonHelper.parameterExists(fieldOfficerIdParamName, element)) {
            final Long fieldOfficerId = this.fromApiJsonHelper.extractLongNamed(fieldOfficerIdParamName, element);
            baseDataValidator.reset().parameter(fieldOfficerIdParamName).value(fieldOfficerId).ignoreIfNull().integerGreaterThanZero();
        }

        if (this.fromApiJsonHelper.parameterExists(submittedOnDateParamName, element)) {
            final LocalDate submittedOnDate = this.fromApiJsonHelper.extractLocalDateNamed(submittedOnDateParamName, element);
            baseDataValidator.reset().parameter(submittedOnDateParamName).value(submittedOnDate).notNull();
        }

        if (this.fromApiJsonHelper.parameterExists(accountNoParamName, element)) {
            final String accountNo = this.fromApiJsonHelper.extractStringNamed(accountNoParamName, element);
            baseDataValidator.reset().parameter(accountNoParamName).value(accountNo).notBlank().notExceedingLengthOf(20);
        }

        if (this.fromApiJsonHelper.parameterExists(externalIdParamName, element)) {
            final String externalId = this.fromApiJsonHelper.extractStringNamed(externalIdParamName, element);
            baseDataValidator.reset().parameter(externalIdParamName).value(externalId).notExceedingLengthOf(100);
        }

        if (this.fromApiJsonHelper.parameterExists(nominalAnnualInterestRateParamName, element)) {
            final BigDecimal interestRate = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed(nominalAnnualInterestRateParamName,
                    element);
            baseDataValidator.reset().parameter(nominalAnnualInterestRateParamName).value(interestRate).notNull().zeroOrPositiveAmount();
        }

        if (this.fromApiJsonHelper.parameterExists(interestCompoundingPeriodTypeParamName, element)) {
            final Integer interestCompoundingPeriodType = this.fromApiJsonHelper
                    .extractIntegerSansLocaleNamed(interestCompoundingPeriodTypeParamName, element);
            baseDataValidator.reset().parameter(interestCompoundingPeriodTypeParamName).value(interestCompoundingPeriodType).notNull()
                    .isOneOfTheseValues(SavingsCompoundingInterestPeriodType.integerValues());
        }

        if (this.fromApiJsonHelper.parameterExists(interestPostingPeriodTypeParamName, element)) {
            final Integer interestPostingPeriodType = this.fromApiJsonHelper
                    .extractIntegerSansLocaleNamed(interestPostingPeriodTypeParamName, element);
            baseDataValidator.reset().parameter(interestPostingPeriodTypeParamName).value(interestPostingPeriodType).notNull()
                    .isOneOfTheseValues(SavingsPostingInterestPeriodType.integerValues());
        }

        if (this.fromApiJsonHelper.parameterExists(interestCalculationTypeParamName, element)) {
            final Integer interestCalculationType = this.fromApiJsonHelper.extractIntegerSansLocaleNamed(interestCalculationTypeParamName,
                    element);
            baseDataValidator.reset().parameter(interestCalculationTypeParamName).value(interestCalculationType).notNull()
                    .isOneOfTheseValues(SavingsInterestCalculationType.integerValues());
        }

        if (this.fromApiJsonHelper.parameterExists(interestCalculationDaysInYearTypeParamName, element)) {
            final Integer interestCalculationDaysInYearType = this.fromApiJsonHelper
                    .extractIntegerSansLocaleNamed(interestCalculationDaysInYearTypeParamName, element);
            baseDataValidator.reset().parameter(interestCalculationDaysInYearTypeParamName).value(interestCalculationDaysInYearType)
                    .notNull().isOneOfTheseValues(SavingsInterestCalculationDaysInYearType.integerValues());
        }

        if (this.fromApiJsonHelper.parameterExists(minRequiredOpeningBalanceParamName, element)) {
            final BigDecimal minOpeningBalance = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed(minRequiredOpeningBalanceParamName,
                    element);
            baseDataValidator.reset().parameter(minRequiredOpeningBalanceParamName).value(minOpeningBalance).ignoreIfNull()
                    .zeroOrPositiveAmount();
        }

        if (this.fromApiJsonHelper.parameterExists(lockinPeriodFrequencyParamName, element)) {
            final Integer lockinPeriodFrequency = this.fromApiJsonHelper.extractIntegerWithLocaleNamed(lockinPeriodFrequencyParamName,
                    element);
            baseDataValidator.reset().parameter(lockinPeriodFrequencyParamName).value(lockinPeriodFrequency).ignoreIfNull()
                    .integerZeroOrGreater();
        }

        if (this.fromApiJsonHelper.parameterExists(lockinPeriodFrequencyTypeParamName, element)) {
            final Integer lockinPeriodFrequencyType = this.fromApiJsonHelper
                    .extractIntegerSansLocaleNamed(lockinPeriodFrequencyTypeParamName, element);
            baseDataValidator.reset().parameter(lockinPeriodFrequencyTypeParamName).value(lockinPeriodFrequencyType).inMinMaxRange(0, 3);
        }

        boolean isLinkedAccRequired = false;
        if (fromApiJsonHelper.parameterExists(transferInterestToSavingsParamName, element)) {
            isLinkedAccRequired = fromApiJsonHelper.extractBooleanNamed(transferInterestToSavingsParamName, element);
        }

        if (this.fromApiJsonHelper.parameterExists(linkedAccountParamName, element)) {
            final Long linkAccountId = this.fromApiJsonHelper.extractLongNamed(linkedAccountParamName, element);
            if (isLinkedAccRequired) {
                baseDataValidator.reset().parameter(linkedAccountParamName).value(linkAccountId).notNull().longGreaterThanZero();
            } else {
                baseDataValidator.reset().parameter(linkedAccountParamName).value(linkAccountId).ignoreIfNull().longGreaterThanZero();
            }
        }

        if (this.fromApiJsonHelper.parameterExists(maturityInstructionIdParamName, element)) {
            final Integer depositRolloverId = this.fromApiJsonHelper.extractIntegerSansLocaleNamed(maturityInstructionIdParamName, element);
            baseDataValidator.reset().parameter(maturityInstructionIdParamName).value(depositRolloverId).notNull()
                    .isOneOfTheseValues(DepositAccountOnClosureType.integerValues());

            if (depositRolloverId.equals(DepositAccountOnClosureType.TRANSFER_TO_SAVINGS.getValue())) {
                final Long transferToSavingsId = this.fromApiJsonHelper.extractLongNamed(transferToSavingsIdParamName, element);
                baseDataValidator.reset().parameter(transferToSavingsIdParamName).value(transferToSavingsId).notNull()
                        .longGreaterThanZero();
            }
        }

    }

    private void validatePreClosureDetailForSubmit(final JsonElement element, final DataValidatorBuilder baseDataValidator) {

        if (fromApiJsonHelper.parameterExists(preClosurePenalApplicableParamName, element)) {
            final boolean preClosurePenalApplicable = fromApiJsonHelper.extractBooleanNamed(preClosurePenalApplicableParamName, element);

            if (preClosurePenalApplicable) {
                final BigDecimal penalInterestRate = fromApiJsonHelper.extractBigDecimalWithLocaleNamed(preClosurePenalInterestParamName,
                        element);
                baseDataValidator.reset().parameter(preClosurePenalInterestParamName).value(penalInterestRate)
                        .cantBeBlankWhenParameterProvidedIs(preClosurePenalApplicableParamName, preClosurePenalApplicable)
                        .zeroOrPositiveAmount();

                final Integer preClosurePenalInterestType = this.fromApiJsonHelper
                        .extractIntegerSansLocaleNamed(preClosurePenalInterestOnTypeIdParamName, element);
                baseDataValidator.reset().parameter(preClosurePenalInterestOnTypeIdParamName).value(preClosurePenalInterestType)
                        .cantBeBlankWhenParameterProvidedIs(preClosurePenalApplicableParamName, preClosurePenalApplicable)
                        .isOneOfTheseValues(PreClosurePenalInterestOnType.integerValues());
            }
        }
    }

    private void validatePreClosureDetailForUpdate(final JsonElement element, final DataValidatorBuilder baseDataValidator) {
        this.productDataValidator.validatePreClosureDetailForUpdate(element, baseDataValidator);
    }

    private void validateDepositTermDeatilForSubmit(final JsonElement element, final DataValidatorBuilder baseDataValidator,
            final DepositAccountType depositType) {

        Integer minTerm = null;
        if (fromApiJsonHelper.parameterExists(minDepositTermParamName, element)) {
            minTerm = fromApiJsonHelper.extractIntegerSansLocaleNamed(minDepositTermParamName, element);
            baseDataValidator.reset().parameter(minDepositTermParamName).value(minTerm).integerGreaterThanZero();
        }

        if (fromApiJsonHelper.parameterExists(maxDepositTermParamName, element)) {
            final Integer maxTerm = fromApiJsonHelper.extractIntegerSansLocaleNamed(maxDepositTermParamName, element);
            baseDataValidator.reset().parameter(maxDepositTermParamName).value(maxTerm).integerGreaterThanZero();
        }

        if (fromApiJsonHelper.parameterExists(minDepositTermTypeIdParamName, element)) {
            final Integer minDepositTermType = this.fromApiJsonHelper.extractIntegerSansLocaleNamed(minDepositTermTypeIdParamName, element);
            baseDataValidator.reset().parameter(minDepositTermTypeIdParamName).value(minDepositTermType)
                    .isOneOfTheseValues(SavingsPeriodFrequencyType.integerValues());
        }

        if (fromApiJsonHelper.parameterExists(maxDepositTermTypeIdParamName, element)) {
            final Integer maxDepositTermType = this.fromApiJsonHelper.extractIntegerSansLocaleNamed(maxDepositTermTypeIdParamName, element);
            baseDataValidator.reset().parameter(maxDepositTermTypeIdParamName).value(maxDepositTermType)
                    .isOneOfTheseValues(SavingsPeriodFrequencyType.integerValues());
        }

        if (fromApiJsonHelper.parameterExists(inMultiplesOfDepositTermParamName, element)) {
            final Integer inMultiplesOfDepositTerm = fromApiJsonHelper.extractIntegerSansLocaleNamed(inMultiplesOfDepositTermParamName,
                    element);
            baseDataValidator.reset().parameter(inMultiplesOfDepositTermParamName).value(inMultiplesOfDepositTerm).integerGreaterThanZero();
            final Integer inMultiplesOfDepositTermType = this.fromApiJsonHelper
                    .extractIntegerSansLocaleNamed(inMultiplesOfDepositTermTypeIdParamName, element);
            baseDataValidator.reset().parameter(inMultiplesOfDepositTermTypeIdParamName).value(inMultiplesOfDepositTermType)
                    .cantBeBlankWhenParameterProvidedIs(inMultiplesOfDepositTermParamName, inMultiplesOfDepositTerm)
                    .isOneOfTheseValues(SavingsPeriodFrequencyType.integerValues());
        }

        // for recurring deposit the total deposit amount is derived from
        // recurring deposit amount * number of deposits.
        if (depositType.isFixedDeposit()) {
            final BigDecimal depositAmount = fromApiJsonHelper.extractBigDecimalWithLocaleNamed(depositAmountParamName, element);
            baseDataValidator.reset().parameter(depositAmountParamName).value(depositAmount).notNull().positiveAmount();
        }

        if (depositType.isFixedDeposit() || fromApiJsonHelper.parameterExists(depositPeriodParamName, element)) {
            final Integer depositPeriod = fromApiJsonHelper.extractIntegerSansLocaleNamed(depositPeriodParamName, element);
            baseDataValidator.reset().parameter(depositPeriodParamName).value(depositPeriod).notNull().integerGreaterThanZero();
        }

        final Integer depositPeriodFrequencyId = this.fromApiJsonHelper.extractIntegerSansLocaleNamed(depositPeriodFrequencyIdParamName,
                element);
        baseDataValidator.reset().parameter(depositPeriodFrequencyIdParamName).value(depositPeriodFrequencyId)
                .isOneOfTheseValues(SavingsPeriodFrequencyType.integerValues());
    }

    private void validateDepositTermDeatilForUpdate(final JsonElement element, final DataValidatorBuilder baseDataValidator,
            final DepositAccountType depositAccountType) {
        this.productDataValidator.validateDepositTermDetailForUpdate(element, baseDataValidator);

        if (fromApiJsonHelper.parameterExists(depositAmountParamName, element)) {
            final BigDecimal depositAmount = fromApiJsonHelper.extractBigDecimalWithLocaleNamed(depositAmountParamName, element);
            baseDataValidator.reset().parameter(depositAmountParamName).value(depositAmount).notNull().positiveAmount();
        }

        if (fromApiJsonHelper.parameterExists(depositPeriodParamName, element)) {
            final Integer depositPeriod = fromApiJsonHelper.extractIntegerSansLocaleNamed(depositPeriodParamName, element);
            if (depositAccountType.isFixedDeposit()) {
                baseDataValidator.reset().parameter(depositPeriodParamName).value(depositPeriod).notNull().integerGreaterThanZero();
            } else {
                baseDataValidator.reset().parameter(depositPeriodParamName).value(depositPeriod).notNull().integerGreaterThanZero();
            }
        }

        if (fromApiJsonHelper.parameterExists(depositPeriodFrequencyIdParamName, element)) {
            final Integer depositPeriodFrequencyId = this.fromApiJsonHelper.extractIntegerSansLocaleNamed(depositPeriodFrequencyIdParamName,
                    element);
            baseDataValidator.reset().parameter(depositPeriodFrequencyIdParamName).value(depositPeriodFrequencyId)
                    .isOneOfTheseValues(SavingsPeriodFrequencyType.integerValues());
        }
    }

    private void validateForInterestCalc(final JsonElement element, final DataValidatorBuilder baseDataValidator) {

        BigDecimal principalAmount = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed(principalAmountParamName, element);
        baseDataValidator.reset().parameter(principalAmountParamName).value(principalAmount).notNull().positiveAmount();

        BigDecimal annualInterestRate = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed(annualInterestRateParamName, element);
        baseDataValidator.reset().parameter(annualInterestRateParamName).value(annualInterestRate).notNull()
                .notLessThanMin(BigDecimal.valueOf(0));

        Long tenureInMonths = this.fromApiJsonHelper.extractLongNamed(tenureInMonthsParamName, element);
        baseDataValidator.reset().parameter(tenureInMonthsParamName).value(tenureInMonths).notNull().longGreaterThanZero();

        Long interestPostingPeriodInMonths = this.fromApiJsonHelper.extractLongNamed(interestPostingPeriodInMonthsParamName, element);
        baseDataValidator.reset().parameter(interestPostingPeriodInMonthsParamName).value(interestPostingPeriodInMonths).notNull()
                .longGreaterThanZero();

        Long interestCompoundingPeriodInMonths = this.fromApiJsonHelper.extractLongNamed(interestCompoundingPeriodInMonthsParamName,
                element);
        baseDataValidator.reset().parameter(interestCompoundingPeriodInMonthsParamName).value(interestCompoundingPeriodInMonths).notNull();

        List<Long> allowedValues = List.of(1L, 2L, 3L, 4L, 6L, 12L);
        if (interestCompoundingPeriodInMonths != null && !allowedValues.contains(interestCompoundingPeriodInMonths)) {
            baseDataValidator.reset().parameter(interestCompoundingPeriodInMonthsParamName).value(interestCompoundingPeriodInMonths)
                    .failWithCode("parameter.value.error", "interestCompoundingPeriodInMonths can only be one {1,2,3,4,6,12}");
        }
        if (interestCompoundingPeriodInMonths != null && tenureInMonths != null
                && tenureInMonths % interestCompoundingPeriodInMonths != 0) {
            baseDataValidator.reset().parameter(interestCompoundingPeriodInMonthsParamName).value(interestCompoundingPeriodInMonths)
                    .failWithCode("parameter.relational.validation",
                            "tenureInMonths must be perfectly divisible by interestCompoundingPeriodInMonths");
        }

    }

    private void validateRecurringDetailForSubmit(final JsonElement element, final DataValidatorBuilder baseDataValidator) {

        final BigDecimal mandatoryRecommendedDepositAmount = fromApiJsonHelper
                .extractBigDecimalWithLocaleNamed(mandatoryRecommendedDepositAmountParamName, element);
        baseDataValidator.reset().parameter(mandatoryRecommendedDepositAmountParamName).value(mandatoryRecommendedDepositAmount).notNull()
                .positiveAmount();

        if (this.fromApiJsonHelper.parameterExists(isMandatoryDepositParamName, element)) {
            final Boolean isMandatoryDeposit = this.fromApiJsonHelper.extractBooleanNamed(isMandatoryDepositParamName, element);
            baseDataValidator.reset().parameter(isMandatoryDepositParamName).value(isMandatoryDeposit).ignoreIfNull()
                    .validateForBooleanValue();
        }

        if (this.fromApiJsonHelper.parameterExists(allowWithdrawalParamName, element)) {
            final Boolean allowWithdrawal = this.fromApiJsonHelper.extractBooleanNamed(allowWithdrawalParamName, element);
            baseDataValidator.reset().parameter(allowWithdrawalParamName).value(allowWithdrawal).ignoreIfNull().validateForBooleanValue();
        }

        if (this.fromApiJsonHelper.parameterExists(adjustAdvanceTowardsFuturePaymentsParamName, element)) {
            final Boolean adjustAdvanceTowardsFuturePayments = this.fromApiJsonHelper
                    .extractBooleanNamed(adjustAdvanceTowardsFuturePaymentsParamName, element);
            baseDataValidator.reset().parameter(adjustAdvanceTowardsFuturePaymentsParamName).value(adjustAdvanceTowardsFuturePayments)
                    .ignoreIfNull().validateForBooleanValue();
        }

        // First validate if string is empty
        final String isCalendarInheritedString = this.fromApiJsonHelper.extractStringNamed(isCalendarInheritedParamName, element);
        if (StringUtils.isBlank(isCalendarInheritedString)) {
            baseDataValidator.reset().parameter(isCalendarInheritedParamName).value(isCalendarInheritedString).notBlank();
        } else {
            // validate the boolean value
            final Boolean isCalendarInherited = this.fromApiJsonHelper.extractBooleanNamed(isCalendarInheritedParamName, element);
            baseDataValidator.reset().parameter(isCalendarInheritedParamName).value(isCalendarInherited).notNull()
                    .validateForBooleanValue();
            if (!isCalendarInherited) {
                final Integer frequencyType = this.fromApiJsonHelper.extractIntegerSansLocaleNamed(recurringFrequencyTypeParamName,
                        element);
                baseDataValidator.reset().parameter(recurringFrequencyTypeParamName).value(frequencyType).notBlank()
                        .isOneOfTheseValues(SavingsPeriodFrequencyType.integerValues());

                final Integer frequency = this.fromApiJsonHelper.extractIntegerSansLocaleNamed(recurringFrequencyParamName, element);
                baseDataValidator.reset().parameter(recurringFrequencyParamName).value(frequency).notNull().integerGreaterThanZero();

                /*
                 * if (CalendarFrequencyType.fromInt(frequency).isWeekly()) { final Integer repeatsOnDay =
                 * this.fromApiJsonHelper.extractIntegerSansLocaleNamed (repeatsOnDayParamName, element);
                 * baseDataValidator.reset().parameter (repeatsOnDayParamName).value(repeatsOnDay).notBlank()
                 * .inMinMaxRange(CalendarWeekDaysType.getMinValue(), CalendarWeekDaysType.getMaxValue()); }
                 */
            }
        }
    }

    private void validateRecurringDetailForUpdate(final JsonElement element, final DataValidatorBuilder baseDataValidator) {
        if (this.fromApiJsonHelper.parameterExists(isMandatoryDepositParamName, element)) {
            final Boolean isMandatoryDeposit = this.fromApiJsonHelper.extractBooleanNamed(isMandatoryDepositParamName, element);
            baseDataValidator.reset().parameter(isMandatoryDepositParamName).value(isMandatoryDeposit).ignoreIfNull()
                    .validateForBooleanValue();
        }

        if (this.fromApiJsonHelper.parameterExists(allowWithdrawalParamName, element)) {
            final Boolean allowWithdrawal = this.fromApiJsonHelper.extractBooleanNamed(allowWithdrawalParamName, element);
            baseDataValidator.reset().parameter(allowWithdrawalParamName).value(allowWithdrawal).ignoreIfNull().validateForBooleanValue();
        }

        if (this.fromApiJsonHelper.parameterExists(adjustAdvanceTowardsFuturePaymentsParamName, element)) {
            final Boolean adjustAdvanceTowardsFuturePayments = this.fromApiJsonHelper
                    .extractBooleanNamed(adjustAdvanceTowardsFuturePaymentsParamName, element);
            baseDataValidator.reset().parameter(adjustAdvanceTowardsFuturePaymentsParamName).value(adjustAdvanceTowardsFuturePayments)
                    .ignoreIfNull().validateForBooleanValue();
        }

        if (this.fromApiJsonHelper.parameterExists(isCalendarInheritedParamName, element)) {
            final Boolean isCalendarInherited = this.fromApiJsonHelper.extractBooleanNamed(isCalendarInheritedParamName, element);
            baseDataValidator.reset().parameter(isCalendarInheritedParamName).value(isCalendarInherited).notNull()
                    .validateForBooleanValue();
        }

        if (this.fromApiJsonHelper.parameterExists(recurringFrequencyTypeParamName, element)) {
            final Integer frequencyType = this.fromApiJsonHelper.extractIntegerSansLocaleNamed(recurringFrequencyTypeParamName, element);
            baseDataValidator.reset().parameter(recurringFrequencyTypeParamName).value(frequencyType).notBlank()
                    .isOneOfTheseValues(SavingsPeriodFrequencyType.integerValues());
        }

        if (this.fromApiJsonHelper.parameterExists(recurringFrequencyParamName, element)) {
            final Integer frequency = this.fromApiJsonHelper.extractIntegerSansLocaleNamed(recurringFrequencyParamName, element);
            baseDataValidator.reset().parameter(recurringFrequencyParamName).value(frequency).notNull().integerGreaterThanZero();
            /*
             * if (this.fromApiJsonHelper.parameterExists(repeatsOnDayParamName, element)) { if
             * (CalendarFrequencyType.fromInt(frequency).isWeekly()) { final Integer repeatsOnDay =
             * this.fromApiJsonHelper.extractIntegerSansLocaleNamed (repeatsOnDayParamName, element);
             * baseDataValidator.reset().parameter (repeatsOnDayParamName).value(repeatsOnDay).notBlank()
             * .inMinMaxRange(CalendarWeekDaysType.getMinValue(), CalendarWeekDaysType.getMaxValue()); } }
             */
        }
    }

    public void validatelinkedSavingsAccount(final SavingsAccount linkedSavingsAccount, final SavingsAccount savingsAccount) {
        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        if (linkedSavingsAccount.isNotActive()) {
            final ApiParameterError error = ApiParameterError.parameterError("validation.msg.deposit.linked.savings.account.is.not.active",
                    "Linked Savings account with id:" + linkedSavingsAccount.getId() + " is not in active state", "linkAccountId",
                    linkedSavingsAccount.getId());
            dataValidationErrors.add(error);
        } else if (!savingsAccount.clientId().equals(linkedSavingsAccount.clientId())) {
            final ApiParameterError error = ApiParameterError.parameterError(
                    "validation.msg.deposit.linked.savings.account.not.belongs.to.same.client",
                    "Linked Savings account with id:" + linkedSavingsAccount.getId() + " is not belongs to the same client",
                    "linkAccountId", linkedSavingsAccount.getId());
            dataValidationErrors.add(error);
        }
        if (!dataValidationErrors.isEmpty()) {
            throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist", "Validation errors exist.",
                    dataValidationErrors);
        }
    }

    public void throwLinkedAccountRequiredError() {
        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final ApiParameterError error = ApiParameterError.parameterError("validation.msg.fixeddepositaccount.linkAccountId.cannot.be.blank",
                "Linked Savings account required", "linkAccountId");
        dataValidationErrors.add(error);
        throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist", "Validation errors exist.",
                dataValidationErrors);
    }

    private void validateSavingsCharges(final JsonElement element, final DataValidatorBuilder baseDataValidator) {

        if (element.isJsonObject()) {
            final JsonObject topLevelJsonElement = element.getAsJsonObject();
            final Locale locale = this.fromApiJsonHelper.extractLocaleParameter(topLevelJsonElement);
            final String monthDayFormat = this.fromApiJsonHelper.extractMonthDayFormatParameter(topLevelJsonElement);
            if (topLevelJsonElement.has(chargesParamName) && topLevelJsonElement.get(chargesParamName).isJsonArray()) {
                final JsonArray array = topLevelJsonElement.get(chargesParamName).getAsJsonArray();
                for (int i = 0; i < array.size(); i++) {

                    final JsonObject savingsChargeElement = array.get(i).getAsJsonObject();

                    final Long chargeId = this.fromApiJsonHelper.extractLongNamed(chargeIdParamName, savingsChargeElement);
                    baseDataValidator.reset().parameter(chargeIdParamName).value(chargeId).longGreaterThanZero();

                    final BigDecimal amount = this.fromApiJsonHelper.extractBigDecimalNamed(amountParamName, savingsChargeElement, locale);
                    baseDataValidator.reset().parameter(amountParamName).value(amount).notNull().positiveAmount();

                    if (this.fromApiJsonHelper.parameterExists(feeOnMonthDayParamName, savingsChargeElement)) {
                        final MonthDay monthDay = this.fromApiJsonHelper.extractMonthDayNamed(feeOnMonthDayParamName, savingsChargeElement,
                                monthDayFormat, locale);
                        baseDataValidator.reset().parameter(feeOnMonthDayParamName).value(monthDay).notNull();
                    }

                    if (this.fromApiJsonHelper.parameterExists(feeIntervalParamName, savingsChargeElement)) {
                        final Integer feeInterval = this.fromApiJsonHelper.extractIntegerNamed(feeIntervalParamName, savingsChargeElement,
                                Locale.getDefault());
                        baseDataValidator.reset().parameter(feeIntervalParamName).value(feeInterval).notNull().inMinMaxRange(1, 12);
                    }
                }
            }
        }
    }

    private void validateWithHoldTax(final JsonElement element, final DataValidatorBuilder baseDataValidator) {
        if (this.fromApiJsonHelper.parameterExists(withHoldTaxParamName, element)) {
            final String withHoldTax = this.fromApiJsonHelper.extractStringNamed(withHoldTaxParamName, element);
            baseDataValidator.reset().parameter(withHoldTaxParamName).value(withHoldTax).ignoreIfNull().validateForBooleanValue();
        }
    }

    private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
        if (!dataValidationErrors.isEmpty()) {
            throw new PlatformApiDataValidationException(dataValidationErrors);
        }
    }

}
