package org.coldis.library.test.serialization.positional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import org.coldis.library.serialization.positional.LocalDateSerializer;
import org.coldis.library.serialization.positional.NumberSerializer;
import org.coldis.library.serialization.positional.PositionalAttribute;

/**
 * Caminhao de vantagens request.
 */
public class CaminhaoVantagensRequest implements Serializable {

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = -5595440038179032105L;

	/**
	 * Type.
	 */
	// 1 Tipo de Registro 1 0 1
	private String type;

	/**
	 * When the request was issued.
	 */
	// 2 Data de emissão 8 1 9
	private LocalDate issuedAt;

	/**
	 * Product code.
	 */
	// 3 Codigo do produto 20 9 29
	private String product;

	/**
	 * Started at.
	 */
	// 4 Data início vigência 8 29 37
	private LocalDate startedAt;

	/**
	 * Finished at.
	 */
	// 5 Data fim vigência 8 37 45
	private LocalDate finishedAt;

	/**
	 * Lucky number.
	 */
	// 6 Numero_Sorte 15 45 60
	private String luckyNumber;

	/**
	 * Person name.
	 */
	// 7 Nome segurado 70 60 130
	private String personName;

	/**
	 * Person kind.
	 */
	// 8 Tipo pessoa 1 130 131
	private String personKind;

	// /**
	// * Empty field.
	// */
	// private String emptyField1 = "";

	/**
	 * Person tax id.
	 */
	// 9 Cpf 11 135 146
	private Long personTaxId;

	// 10 Endereço 50 146 196
	private String personStreetAddress;

	// 11 Número 10 196 206
	private Long personStreetNumber;

	// 12 Complemento 20 206 226
	private String personStreetComplement;

	/**
	 * Neighborhood.
	 */
	// 13 Bairro 50 226 276
	private String personNeighborhood;

	// 14 Cidade 50 276 326
	private String personCity;

	// 15 UF 2 326 328
	private String personState;

	// 16 CEP 8 328 336
	private Long personZipCode;

	// 17 DDD 2 336 338
	private Long personPhoneNumberAreaCode;

	// 18 Telefone 10 338 348
	private Long personPhoneNumber;

	// 19 RG 15 348 363
	private String personIdNumber;

	// 20 UF_RG 2 363 36
	private String personIdIssueState;

	// 21 Prêmio líquido 10 365 375
	private BigDecimal premium;

	// 22 IOF 10 375 385
	private BigDecimal taxes;

	// 23 Prêmio 10 385 395
	private BigDecimal premiumWithTaxes;

	// 24 Forma de pagamento 1 395 396
	private String paymentMethod;

	// 25 Quantidade de parcelas 2 396 398
	private Long installments;

	// 26 Valor parcela 10 398 408
	private BigDecimal installmentAmount;

	// // 27 Nº conta cartão NÃO OBRIGATÓRIO 20 408 428
	// private String emptyField2 = "";
	//
	// // 28 Tipo cartão NÃO OBRIGATÓRIO 1 428 429
	// private String emptyField3 = "";

	// 29 Nome produto CAMINHAO 100 429 529
	private String productName;

	// 30 Valor venda 10 529 539
	private BigDecimal saleAmount;

	// 31 Data venda 8 539 547
	private LocalDate saleDate;

	// private String emptyField4;

	// 32 Data nascimento 8 797 805
	private LocalDate personBirthDate;

	// 33 Estado civil 1 805 806
	private String personCivilStatus;

	// 34 Sexo 4 806 810
	private String personSex;

	// 35 Email 50 832 882
	private String personEmailAddress;

	// // 36 Id loja NÃO OBRIGATÓRIO 5 892 897
	// private String emptyField5 = "";

	// 37 Data vencimento 9 897 906
	private LocalDate dueDate;

	/**
	 * Gets the type.
	 *
	 * @return The type.
	 */
	@PositionalAttribute(
			initialPosition = 0,
			finalPosition = 1
	)
	public String getType() {
		return this.type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type New type.
	 */
	public void setType(
			final String type) {
		this.type = type;
	}

	/**
	 * Gets the issuedAt.
	 *
	 * @return The issuedAt.
	 */
	@PositionalAttribute(
			initialPosition = 1,
			finalPosition = 9,
			positionalSerializerInterface = LocalDateSerializer.class,
			postionalDeserializerInterface = LocalDateSerializer.class,
			serializerInitParam = "ddMMyyy",
			deserializerInitParam = "ddMMyyy"
	)
	public LocalDate getIssuedAt() {
		return this.issuedAt;
	}

	/**
	 * Sets the issuedAt.
	 *
	 * @param issuedAt New issuedAt.
	 */
	public void setIssuedAt(
			final LocalDate issuedAt) {
		this.issuedAt = issuedAt;
	}

	/**
	 * Gets the product.
	 *
	 * @return The product.
	 */
	@PositionalAttribute(
			initialPosition = 9,
			finalPosition = 29
	)
	public String getProduct() {
		return this.product;
	}

	/**
	 * Sets the product.
	 *
	 * @param product New product.
	 */
	public void setProduct(
			final String product) {
		this.product = product;
	}

	/**
	 * Gets the startedAt.
	 *
	 * @return The startedAt.
	 */
	@PositionalAttribute(
			initialPosition = 29,
			finalPosition = 37,
			positionalSerializerInterface = LocalDateSerializer.class,
			postionalDeserializerInterface = LocalDateSerializer.class,
			serializerInitParam = "ddMMyyy",
			deserializerInitParam = "ddMMyyy"
	)
	public LocalDate getStartedAt() {
		return this.startedAt;
	}

	/**
	 * Sets the startedAt.
	 *
	 * @param startedAt New startedAt.
	 */
	public void setStartedAt(
			final LocalDate startedAt) {
		this.startedAt = startedAt;
	}

	/**
	 * Gets the finishedAt.
	 *
	 * @return The finishedAt.
	 */
	@PositionalAttribute(
			initialPosition = 37,
			finalPosition = 45,
			positionalSerializerInterface = LocalDateSerializer.class,
			postionalDeserializerInterface = LocalDateSerializer.class,
			serializerInitParam = "ddMMyyy",
			deserializerInitParam = "ddMMyyy"
	)
	public LocalDate getFinishedAt() {
		return this.finishedAt;
	}

	/**
	 * Sets the finishedAt.
	 *
	 * @param finishedAt New finishedAt.
	 */
	public void setFinishedAt(
			final LocalDate finishedAt) {
		this.finishedAt = finishedAt;
	}

	/**
	 * Gets the luckyNumber.
	 *
	 * @return The luckyNumber.
	 */
	@PositionalAttribute(
			initialPosition = 45,
			finalPosition = 60
	)
	public String getLuckyNumber() {
		return this.luckyNumber;
	}

	/**
	 * Sets the luckyNumber.
	 *
	 * @param luckyNumber New luckyNumber.
	 */
	public void setLuckyNumber(
			final String luckyNumber) {
		this.luckyNumber = luckyNumber;
	}

	/**
	 * Gets the personName.
	 *
	 * @return The personName.
	 */
	@PositionalAttribute(
			initialPosition = 60,
			finalPosition = 130
	)
	public String getPersonName() {
		return this.personName;
	}

	/**
	 * Sets the personName.
	 *
	 * @param personName New personName.
	 */
	public void setPersonName(
			final String personName) {
		this.personName = personName;
	}

	/**
	 * Gets the personKind.
	 *
	 * @return The personKind.
	 */
	@PositionalAttribute(
			initialPosition = 130,
			finalPosition = 131
	)
	public String getPersonKind() {
		return this.personKind;
	}

	/**
	 * Sets the personKind.
	 *
	 * @param personKind New personKind.
	 */
	public void setPersonKind(
			final String personKind) {
		this.personKind = personKind;
	}

	/**
	 * Gets the personTaxId.
	 *
	 * @return The personTaxId.
	 */
	@PositionalAttribute(
			fillLeft = true,
			initialPosition = 135,
			finalPosition = 146,
			positionalSerializerInterface = NumberSerializer.class,
			postionalDeserializerInterface = NumberSerializer.class,
			serializerInitParam = "#/pt_BR/false/1",
			deserializerInitParam = "#/pt_BR/false/1"
	)
	public Long getPersonTaxId() {
		return this.personTaxId;
	}

	/**
	 * Sets the personTaxId.
	 *
	 * @param personTaxId New personTaxId.
	 */
	public void setPersonTaxId(
			final Long personTaxId) {
		this.personTaxId = personTaxId;
	}

	/**
	 * Gets the personStreetAddress.
	 *
	 * @return The personStreetAddress.
	 */
	@PositionalAttribute(
			initialPosition = 146,
			finalPosition = 196
	)
	public String getPersonStreetAddress() {
		return this.personStreetAddress;
	}

	/**
	 * Sets the personStreetAddress.
	 *
	 * @param personStreetAddress New personStreetAddress.
	 */
	public void setPersonStreetAddress(
			final String personStreetAddress) {
		this.personStreetAddress = personStreetAddress;
	}

	/**
	 * Gets the personStreetNumber.
	 *
	 * @return The personStreetNumber.
	 */
	@PositionalAttribute(
			fillLeft = true,
			initialPosition = 196,
			finalPosition = 206,
			positionalSerializerInterface = NumberSerializer.class,
			postionalDeserializerInterface = NumberSerializer.class,
			serializerInitParam = "#/pt_BR/false/1",
			deserializerInitParam = "#/pt_BR/false/1"
	)
	public Long getPersonStreetNumber() {
		return this.personStreetNumber;
	}

	/**
	 * Sets the personStreetNumber.
	 *
	 * @param personStreetNumber New personStreetNumber.
	 */
	public void setPersonStreetNumber(
			final Long personStreetNumber) {
		this.personStreetNumber = personStreetNumber;
	}

	/**
	 * Gets the personStreetComplement.
	 *
	 * @return The personStreetComplement.
	 */
	@PositionalAttribute(
			initialPosition = 206,
			finalPosition = 226
	)
	public String getPersonStreetComplement() {
		return this.personStreetComplement;
	}

	/**
	 * Sets the personStreetComplement.
	 *
	 * @param personStreetComplement New personStreetComplement.
	 */
	public void setPersonStreetComplement(
			final String personStreetComplement) {
		this.personStreetComplement = personStreetComplement;
	}

	/**
	 * Gets the personNeighborhood.
	 *
	 * @return The personNeighborhood.
	 */
	@PositionalAttribute(
			initialPosition = 226,
			finalPosition = 276
	)
	public String getPersonNeighborhood() {
		return this.personNeighborhood;
	}

	/**
	 * Sets the personNeighborhood.
	 *
	 * @param personNeighborhood New personNeighborhood.
	 */
	public void setPersonNeighborhood(
			final String personNeighborhood) {
		this.personNeighborhood = personNeighborhood;
	}

	/**
	 * Gets the personCity.
	 *
	 * @return The personCity.
	 */
	@PositionalAttribute(
			initialPosition = 276,
			finalPosition = 326
	)
	public String getPersonCity() {
		return this.personCity;
	}

	/**
	 * Sets the personCity.
	 *
	 * @param personCity New personCity.
	 */
	public void setPersonCity(
			final String personCity) {
		this.personCity = personCity;
	}

	/**
	 * Gets the personState.
	 *
	 * @return The personState.
	 */
	@PositionalAttribute(
			initialPosition = 326,
			finalPosition = 328
	)
	public String getPersonState() {
		return this.personState;
	}

	/**
	 * Sets the personState.
	 *
	 * @param personState New personState.
	 */
	public void setPersonState(
			final String personState) {
		this.personState = personState;
	}

	/**
	 * Gets the personZipCode.
	 *
	 * @return The personZipCode.
	 */
	@PositionalAttribute(
			fillLeft = true,
			initialPosition = 328,
			finalPosition = 336,
			positionalSerializerInterface = NumberSerializer.class,
			postionalDeserializerInterface = NumberSerializer.class,
			serializerInitParam = "#/pt_BR/false/1",
			deserializerInitParam = "#/pt_BR/false/1"
	)
	public Long getPersonZipCode() {
		return this.personZipCode;
	}

	/**
	 * Sets the personZipCode.
	 *
	 * @param personZipCode New personZipCode.
	 */
	public void setPersonZipCode(
			final Long personZipCode) {
		this.personZipCode = personZipCode;
	}

	/**
	 * Gets the personPhoneNumberAreaCode.
	 *
	 * @return The personPhoneNumberAreaCode.
	 */
	@PositionalAttribute(
			fillLeft = true,
			initialPosition = 336,
			finalPosition = 338,
			positionalSerializerInterface = NumberSerializer.class,
			postionalDeserializerInterface = NumberSerializer.class,
			serializerInitParam = "#/pt_BR/false/1",
			deserializerInitParam = "#/pt_BR/false/1"
	)
	public Long getPersonPhoneNumberAreaCode() {
		return this.personPhoneNumberAreaCode;
	}

	/**
	 * Sets the personPhoneNumberAreaCode.
	 *
	 * @param personPhoneNumberAreaCode New personPhoneNumberAreaCode.
	 */
	public void setPersonPhoneNumberAreaCode(
			final Long personPhoneNumberAreaCode) {
		this.personPhoneNumberAreaCode = personPhoneNumberAreaCode;
	}

	/**
	 * Gets the personPhoneNumber.
	 *
	 * @return The personPhoneNumber.
	 */
	@PositionalAttribute(
			fillLeft = true,
			initialPosition = 338,
			finalPosition = 348,
			positionalSerializerInterface = NumberSerializer.class,
			postionalDeserializerInterface = NumberSerializer.class,
			serializerInitParam = "#/pt_BR/false/1",
			deserializerInitParam = "#/pt_BR/false/1"
	)
	public Long getPersonPhoneNumber() {
		return this.personPhoneNumber;
	}

	/**
	 * Sets the personPhoneNumber.
	 *
	 * @param personPhoneNumber New personPhoneNumber.
	 */
	public void setPersonPhoneNumber(
			final Long personPhoneNumber) {
		this.personPhoneNumber = personPhoneNumber;
	}

	/**
	 * Gets the personIdNumber.
	 *
	 * @return The personIdNumber.
	 */
	@PositionalAttribute(
			initialPosition = 348,
			finalPosition = 363
	)
	public String getPersonIdNumber() {
		return this.personIdNumber;
	}

	/**
	 * Sets the personIdNumber.
	 *
	 * @param personIdNumber New personIdNumber.
	 */
	public void setPersonIdNumber(
			final String personIdNumber) {
		this.personIdNumber = personIdNumber;
	}

	/**
	 * Gets the personIdIssueState.
	 *
	 * @return The personIdIssueState.
	 */
	@PositionalAttribute(
			initialPosition = 363,
			finalPosition = 365
	)
	public String getPersonIdIssueState() {
		return this.personIdIssueState;
	}

	/**
	 * Sets the personIdIssueState.
	 *
	 * @param personIdIssueState New personIdIssueState.
	 */
	public void setPersonIdIssueState(
			final String personIdIssueState) {
		this.personIdIssueState = personIdIssueState;
	}

	/**
	 * Gets the premium.
	 *
	 * @return The premium.
	 */
	@PositionalAttribute(
			fillLeft = true,
			initialPosition = 365,
			finalPosition = 375,
			positionalSerializerInterface = NumberSerializer.class,
			postionalDeserializerInterface = NumberSerializer.class,
			serializerInitParam = "#.00/pt_BR/true/1",
			deserializerInitParam = "#.00/pt_BR/true/1"
	)
	public BigDecimal getPremium() {
		return this.premium;
	}

	/**
	 * Sets the premium.
	 *
	 * @param premium New premium.
	 */
	public void setPremium(
			final BigDecimal premium) {
		this.premium = premium;
	}

	/**
	 * Gets the taxes.
	 *
	 * @return The taxes.
	 */
	@PositionalAttribute(
			fillLeft = true,
			initialPosition = 375,
			finalPosition = 385,
			positionalSerializerInterface = NumberSerializer.class,
			postionalDeserializerInterface = NumberSerializer.class,
			serializerInitParam = "#.00/pt_BR/true/1",
			deserializerInitParam = "#.00/pt_BR/true/1"
	)
	public BigDecimal getTaxes() {
		return this.taxes;
	}

	/**
	 * Sets the taxes.
	 *
	 * @param taxes New taxes.
	 */
	public void setTaxes(
			final BigDecimal taxes) {
		this.taxes = taxes;
	}

	/**
	 * Gets the premiumWithTaxes.
	 *
	 * @return The premiumWithTaxes.
	 */
	@PositionalAttribute(
			fillLeft = true,
			initialPosition = 385,
			finalPosition = 395,
			positionalSerializerInterface = NumberSerializer.class,
			postionalDeserializerInterface = NumberSerializer.class,
			serializerInitParam = "#.00/pt_BR/true/1",
			deserializerInitParam = "#.00/pt_BR/true/1"
	)
	public BigDecimal getPremiumWithTaxes() {
		return this.premiumWithTaxes;
	}

	/**
	 * Sets the premiumWithTaxes.
	 *
	 * @param premiumWithTaxes New premiumWithTaxes.
	 */
	public void setPremiumWithTaxes(
			final BigDecimal premiumWithTaxes) {
		this.premiumWithTaxes = premiumWithTaxes;
	}

	/**
	 * Gets the paymentMethod.
	 *
	 * @return The paymentMethod.
	 */
	@PositionalAttribute(
			initialPosition = 395,
			finalPosition = 396
	)
	public String getPaymentMethod() {
		return this.paymentMethod;
	}

	/**
	 * Sets the paymentMethod.
	 *
	 * @param paymentMethod New paymentMethod.
	 */
	public void setPaymentMethod(
			final String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * Gets the installments.
	 *
	 * @return The installments.
	 */
	@PositionalAttribute(
			fillLeft = true,
			filler = 0,
			initialPosition = 396,
			finalPosition = 398,
			positionalSerializerInterface = NumberSerializer.class,
			postionalDeserializerInterface = NumberSerializer.class,
			serializerInitParam = "#.00/pt_BR/false/1",
			deserializerInitParam = "#.00/pt_BR/false/1"
	)
	public Long getInstallments() {
		return this.installments;
	}

	/**
	 * Sets the installments.
	 *
	 * @param installments New installments.
	 */
	public void setInstallments(
			final Long installments) {
		this.installments = installments;
	}

	/**
	 * Gets the installmentAmount.
	 *
	 * @return The installmentAmount.
	 */
	@PositionalAttribute(
			fillLeft = true,
			filler = 0,
			initialPosition = 398,
			finalPosition = 408,
			positionalSerializerInterface = NumberSerializer.class,
			postionalDeserializerInterface = NumberSerializer.class,
			serializerInitParam = "#.00/pt_BR/true/1",
			deserializerInitParam = "#.00/pt_BR/true/1"
	)
	public BigDecimal getInstallmentAmount() {
		return this.installmentAmount;
	}

	/**
	 * Sets the installmentAmount.
	 *
	 * @param installmentAmount New installmentAmount.
	 */
	public void setInstallmentAmount(
			final BigDecimal installmentAmount) {
		this.installmentAmount = installmentAmount;
	}

	/**
	 * Gets the productName.
	 *
	 * @return The productName.
	 */
	@PositionalAttribute(
			initialPosition = 429,
			finalPosition = 529
	)
	public String getProductName() {
		return this.productName;
	}

	/**
	 * Sets the productName.
	 *
	 * @param productName New productName.
	 */
	public void setProductName(
			final String productName) {
		this.productName = productName;
	}

	/**
	 * Gets the saleAmount.
	 *
	 * @return The saleAmount.
	 */
	@PositionalAttribute(
			fillLeft = true,
			filler = 0,
			initialPosition = 529,
			finalPosition = 539,
			positionalSerializerInterface = NumberSerializer.class,
			postionalDeserializerInterface = NumberSerializer.class,
			serializerInitParam = "#.00/pt_BR/true/1",
			deserializerInitParam = "#.00/pt_BR/true/1"
	)
	public BigDecimal getSaleAmount() {
		return this.saleAmount;
	}

	/**
	 * Sets the saleAmount.
	 *
	 * @param saleAmount New saleAmount.
	 */
	public void setSaleAmount(
			final BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
	}

	/**
	 * Gets the saleDate.
	 *
	 * @return The saleDate.
	 */
	@PositionalAttribute(
			initialPosition = 539,
			finalPosition = 547,
			positionalSerializerInterface = LocalDateSerializer.class,
			postionalDeserializerInterface = LocalDateSerializer.class,
			serializerInitParam = "ddMMyyyy",
			deserializerInitParam = "ddMMyyyy"
	)
	public LocalDate getSaleDate() {
		return this.saleDate;
	}

	/**
	 * Sets the saleDate.
	 *
	 * @param saleDate New saleDate.
	 */
	public void setSaleDate(
			final LocalDate saleDate) {
		this.saleDate = saleDate;
	}

	/**
	 * Gets the personBirthDate.
	 *
	 * @return The personBirthDate.
	 */
	@PositionalAttribute(
			initialPosition = 797,
			finalPosition = 805,
			positionalSerializerInterface = LocalDateSerializer.class,
			postionalDeserializerInterface = LocalDateSerializer.class,
			serializerInitParam = "ddMMyyyy",
			deserializerInitParam = "ddMMyyyy"
	)
	public LocalDate getPersonBirthDate() {
		return this.personBirthDate;
	}

	/**
	 * Sets the personBirthDate.
	 *
	 * @param personBirthDate New personBirthDate.
	 */
	public void setPersonBirthDate(
			final LocalDate personBirthDate) {
		this.personBirthDate = personBirthDate;
	}

	/**
	 * Gets the personCivilStatus.
	 *
	 * @return The personCivilStatus.
	 */
	@PositionalAttribute(
			initialPosition = 805,
			finalPosition = 806
	)
	public String getPersonCivilStatus() {
		return this.personCivilStatus;
	}

	/**
	 * Sets the personCivilStatus.
	 *
	 * @param personCivilStatus New personCivilStatus.
	 */
	public void setPersonCivilStatus(
			final String personCivilStatus) {
		this.personCivilStatus = personCivilStatus;
	}

	/**
	 * Gets the personSex.
	 *
	 * @return The personSex.
	 */
	@PositionalAttribute(
			initialPosition = 806,
			finalPosition = 810
	)
	public String getPersonSex() {
		return this.personSex;
	}

	/**
	 * Sets the personSex.
	 *
	 * @param personSex New personSex.
	 */
	public void setPersonSex(
			final String personSex) {
		this.personSex = personSex;
	}

	/**
	 * Gets the personEmailAddress.
	 *
	 * @return The personEmailAddress.
	 */
	@PositionalAttribute(
			initialPosition = 832,
			finalPosition = 882
	)
	public String getPersonEmailAddress() {
		return this.personEmailAddress;
	}

	/**
	 * Sets the personEmailAddress.
	 *
	 * @param personEmailAddress New personEmailAddress.
	 */
	public void setPersonEmailAddress(
			final String personEmailAddress) {
		this.personEmailAddress = personEmailAddress;
	}

	/**
	 * Gets the dueDate.
	 *
	 * @return The dueDate.
	 */
	@PositionalAttribute(
			initialPosition = 897,
			finalPosition = 906,
			positionalSerializerInterface = LocalDateSerializer.class,
			postionalDeserializerInterface = LocalDateSerializer.class,
			serializerInitParam = "ddMMyyyy",
			deserializerInitParam = "ddMMyyyy"
	)
	public LocalDate getDueDate() {
		return this.dueDate;
	}

	/**
	 * Sets the dueDate.
	 *
	 * @param dueDate New dueDate.
	 */
	public void setDueDate(
			final LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * No arguments constructor.
	 */
	public CaminhaoVantagensRequest() {
		super();
	}

	/**
	 * TODO Javadoc
	 *
	 * @param type
	 * @param issuedAt
	 * @param product
	 * @param startedAt
	 * @param finishedAt
	 * @param luckyNumber
	 * @param personName
	 * @param personKind
	 * @param personTaxId
	 * @param personStreetAddress
	 * @param personStreetNumber
	 * @param personStreetComplement
	 * @param personNeighborhood
	 * @param personCity
	 * @param personState
	 * @param personZipCode
	 * @param personPhoneNumberAreaCode
	 * @param personPhoneNumber
	 * @param personIdNumber
	 * @param personIdIssueState
	 * @param premium
	 * @param taxes
	 * @param premiumWithTaxes
	 * @param paymentMethod
	 * @param installments
	 * @param installmentAmount
	 * @param productName
	 * @param saleAmount
	 * @param saleDate
	 * @param personBirthDate
	 * @param personCivilStatus
	 * @param personSex
	 * @param personEmailAddress
	 * @param dueDate                   Javadoc
	 */
	public CaminhaoVantagensRequest(
			final String type,
			final LocalDate issuedAt,
			final String product,
			final LocalDate startedAt,
			final LocalDate finishedAt,
			final String luckyNumber,
			final String personName,
			final String personKind,
			final Long personTaxId,
			final String personStreetAddress,
			final Long personStreetNumber,
			final String personStreetComplement,
			final String personNeighborhood,
			final String personCity,
			final String personState,
			final Long personZipCode,
			final Long personPhoneNumberAreaCode,
			final Long personPhoneNumber,
			final String personIdNumber,
			final String personIdIssueState,
			final BigDecimal premium,
			final BigDecimal taxes,
			final BigDecimal premiumWithTaxes,
			final String paymentMethod,
			final Long installments,
			final BigDecimal installmentAmount,
			final String productName,
			final BigDecimal saleAmount,
			final LocalDate saleDate,
			final LocalDate personBirthDate,
			final String personCivilStatus,
			final String personSex,
			final String personEmailAddress,
			final LocalDate dueDate) {
		super();
		this.type = type;
		this.issuedAt = issuedAt;
		this.product = product;
		this.startedAt = startedAt;
		this.finishedAt = finishedAt;
		this.luckyNumber = luckyNumber;
		this.personName = personName;
		this.personKind = personKind;
		this.personTaxId = personTaxId;
		this.personStreetAddress = personStreetAddress;
		this.personStreetNumber = personStreetNumber;
		this.personStreetComplement = personStreetComplement;
		this.personNeighborhood = personNeighborhood;
		this.personCity = personCity;
		this.personState = personState;
		this.personZipCode = personZipCode;
		this.personPhoneNumberAreaCode = personPhoneNumberAreaCode;
		this.personPhoneNumber = personPhoneNumber;
		this.personIdNumber = personIdNumber;
		this.personIdIssueState = personIdIssueState;
		this.premium = premium;
		this.taxes = taxes;
		this.premiumWithTaxes = premiumWithTaxes;
		this.paymentMethod = paymentMethod;
		this.installments = installments;
		this.installmentAmount = installmentAmount;
		this.productName = productName;
		this.saleAmount = saleAmount;
		this.saleDate = saleDate;
		this.personBirthDate = personBirthDate;
		this.personCivilStatus = personCivilStatus;
		this.personSex = personSex;
		this.personEmailAddress = personEmailAddress;
		this.dueDate = dueDate;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.dueDate, this.finishedAt, this.installmentAmount, this.installments, this.issuedAt, this.luckyNumber, this.paymentMethod,
				this.personBirthDate, this.personCity, this.personCivilStatus, this.personEmailAddress, this.personIdIssueState, this.personIdNumber,
				this.personKind, this.personName, this.personNeighborhood, this.personPhoneNumber, this.personPhoneNumberAreaCode, this.personSex,
				this.personState, this.personStreetAddress, this.personStreetComplement, this.personStreetNumber, this.personTaxId, this.personZipCode,
				this.premium, this.premiumWithTaxes, this.product, this.productName, this.saleAmount, this.saleDate, this.startedAt, this.taxes, this.type);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(
			final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CaminhaoVantagensRequest)) {
			return false;
		}
		final CaminhaoVantagensRequest other = (CaminhaoVantagensRequest) obj;
		return Objects.equals(this.dueDate, other.dueDate) && Objects.equals(this.finishedAt, other.finishedAt)
				&& Objects.equals(this.installmentAmount, other.installmentAmount) && Objects.equals(this.installments, other.installments)
				&& Objects.equals(this.issuedAt, other.issuedAt) && Objects.equals(this.luckyNumber, other.luckyNumber)
				&& Objects.equals(this.paymentMethod, other.paymentMethod) && Objects.equals(this.personBirthDate, other.personBirthDate)
				&& Objects.equals(this.personCity, other.personCity) && Objects.equals(this.personCivilStatus, other.personCivilStatus)
				&& Objects.equals(this.personEmailAddress, other.personEmailAddress) && Objects.equals(this.personIdIssueState, other.personIdIssueState)
				&& Objects.equals(this.personIdNumber, other.personIdNumber) && Objects.equals(this.personKind, other.personKind)
				&& Objects.equals(this.personName, other.personName) && Objects.equals(this.personNeighborhood, other.personNeighborhood)
				&& Objects.equals(this.personPhoneNumber, other.personPhoneNumber)
				&& Objects.equals(this.personPhoneNumberAreaCode, other.personPhoneNumberAreaCode) && Objects.equals(this.personSex, other.personSex)
				&& Objects.equals(this.personState, other.personState) && Objects.equals(this.personStreetAddress, other.personStreetAddress)
				&& Objects.equals(this.personStreetComplement, other.personStreetComplement)
				&& Objects.equals(this.personStreetNumber, other.personStreetNumber) && Objects.equals(this.personTaxId, other.personTaxId)
				&& Objects.equals(this.personZipCode, other.personZipCode) && Objects.equals(this.premium, other.premium)
				&& Objects.equals(this.premiumWithTaxes, other.premiumWithTaxes) && Objects.equals(this.product, other.product)
				&& Objects.equals(this.productName, other.productName) && Objects.equals(this.saleAmount, other.saleAmount)
				&& Objects.equals(this.saleDate, other.saleDate) && Objects.equals(this.startedAt, other.startedAt) && Objects.equals(this.taxes, other.taxes)
				&& Objects.equals(this.type, other.type);
	}

}
