import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import Card from '../Card/Card';
import "./BacklogDeck.scss";

const BacklogDeck = ({ topMostBacklogCardId, backlogHasCards }) => {
  return <div className="backlog">
    { backlogHasCards && <Card key={topMostBacklogCardId} id={topMostBacklogCardId} /> }
    { !backlogHasCards && <div className="empty">&nbsp;</div> }
  </div>
};

BacklogDeck.propTypes = {
  topMostBacklogCardId: PropTypes.string
};

const mapStateToProps = (state) => {
  const topMostCardId = state.backlogDeck.length > 0 ? state.backlogDeck[0] : null;
  const backlogHasCards = state.backlogDeck.length > 0;
  return {
    topMostBacklogCardId: topMostCardId,
    backlogHasCards: backlogHasCards
  }
};

export default connect(
  mapStateToProps
)(BacklogDeck);
