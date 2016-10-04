import React, { PropTypes } from 'react';
import Card from '../Card/Card';

const ColumnCards = ({ columnCardIds }) => {
  return <div className="ColumnCards">
    {columnCardIds.map(cardId => <Card key={cardId} id={cardId} />)}
  </div>
};

ColumnCards.propTypes = {
  columnCardsIds: PropTypes.arrayOf(PropTypes.string.isRequired)
};

export default ColumnCards;
