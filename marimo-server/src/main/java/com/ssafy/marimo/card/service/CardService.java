package com.ssafy.marimo.card.service;

import com.ssafy.marimo.card.domain.Card;
import com.ssafy.marimo.card.domain.CardBenefit;
import com.ssafy.marimo.card.domain.CardBenefitDetail;
import com.ssafy.marimo.card.domain.GasStationBrand;
import com.ssafy.marimo.card.domain.MemberCard;
import com.ssafy.marimo.card.dto.PostCardRequest;
import com.ssafy.marimo.card.dto.PostCardResponse;
import com.ssafy.marimo.card.repository.CardBenefitDetailRepository;
import com.ssafy.marimo.card.repository.CardBenefitRepository;
import com.ssafy.marimo.card.repository.CardRepository;
import com.ssafy.marimo.card.repository.MemberCardRepository;
import com.ssafy.marimo.common.util.IdEncryptionUtil;
import com.ssafy.marimo.exception.BadRequestException;
import com.ssafy.marimo.exception.ErrorStatus;
import com.ssafy.marimo.exception.NotFoundException;
import com.ssafy.marimo.external.dto.CardInfoDto;
import com.ssafy.marimo.external.dto.FintechCardListResponse;
import com.ssafy.marimo.external.dto.FintechCardListResponse.CardInfo;
import com.ssafy.marimo.external.dto.GetCardsWithBenefitResponse;
import com.ssafy.marimo.external.fintech.FintechApiClient;
import com.ssafy.marimo.member.domain.Member;
import com.ssafy.marimo.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {

    private static final String CATEGORY_GAS = "GAS";

    private final CardRepository cardRepository;
    private final MemberRepository memberRepository;
    private final FintechApiClient fintechApiClient;
    private final CardBenefitRepository cardBenefitRepository;
    private final CardBenefitDetailRepository cardBenefitDetailRepository;
    private final MemberCardRepository memberCardRepository;
    private final IdEncryptionUtil idEncryptionUtil;

    public PostCardResponse postOilCard(PostCardRequest postCardRequest, Integer memberId) {

        Card card = cardRepository.findByCardUniqueNo(postCardRequest.cardUniqueNo())
                .orElseThrow(() -> new NotFoundException(ErrorStatus.CARD_NOT_FOUND.getErrorCode()));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.MEMBER_NOT_FOUND.getErrorCode()));

        if (memberCardRepository.existsByMemberIdAndCardId(memberId, card.getId())) {
            throw new BadRequestException(ErrorStatus.MEMBER_CARD_DUPLICATED.getErrorCode());
        }

        MemberCard memberCard = MemberCard.of(member, card);

        MemberCard savedMemberCard = memberCardRepository.save(memberCard);

        return PostCardResponse.of(idEncryptionUtil.encrypt(savedMemberCard.getId()));

    }

    public GetCardsWithBenefitResponse getCardsWithBenefit() {
        // 1. 외부 카드 목록 조회
        FintechCardListResponse response = fintechApiClient.getRegisteredCards();
        List<CardInfo> cardInfos = response.getRec();

        List<CardInfoDto> cardInfoDtos = new ArrayList<>();

        for (CardInfo cardInfo : cardInfos) {
            // 2. DB에서 카드 정보 조회
            Card card = cardRepository.findByCardUniqueNo(cardInfo.getCardUniqueNo())
                    .orElseThrow(() -> new NotFoundException(ErrorStatus.CARD_NOT_FOUND.getErrorCode()));

            // 3. 주유 관련 혜택 조회
            List<CardBenefit> cardBenefits = cardBenefitRepository.findByCardIdAndCategory(card.getId(), CATEGORY_GAS);

            StringBuilder cardDescription = new StringBuilder();
            for (CardBenefit benefit : cardBenefits) {
                List<CardBenefitDetail> brandDetails =
                        cardBenefitDetailRepository.findByCardBenefitId(benefit.getId());

                for (CardBenefitDetail detail : brandDetails) {
                    if (detail.getAppliesToAllBrands() != null && detail.getAppliesToAllBrands()) {
                        cardDescription.append(String.format(
                                "모든 주유소: %d%s",
                                detail.getDiscountValue(),
                                Optional.ofNullable(detail.getDiscountUnit()).orElse("")
                        ));
                        continue;
                    }
                    GasStationBrand brand = detail.getGasStationBrand();
                    cardDescription.append(String.format(
                            "%s: %d%s\n",
                            brand != null ? brand : "알 수 없음",
                            detail.getDiscountValue(),
                            Optional.ofNullable(detail.getDiscountUnit()).orElse("")
                    ));
                }
            }

            // 4. 전월 실적 정보 포함
            String baselinePerformance = card.getMonthlyRequirement() != null
                    ? String.format("전월 실적 %d원 이상", card.getMonthlyRequirement())
                    : "전월 실적 정보 없음";

            // 5. DTO 조립
            CardInfoDto dto = CardInfoDto.of(
                    cardInfo.getCardUniqueNo(),
                    cardInfo.getCardIssuerName(),
                    cardInfo.getCardName(),
                    cardDescription.toString(),
                    baselinePerformance
            );

            cardInfoDtos.add(dto);
        }

        return GetCardsWithBenefitResponse.of(cardInfoDtos);

    }
}

