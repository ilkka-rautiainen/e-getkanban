import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import Card from '../Card/Card';
import "./BacklogDeck.scss";

const BacklogDeck = ({ topMostBacklogCardId }) => {
  return <div className="backlog">
    { topMostBacklogCardId && <Card key={topMostBacklogCardId} id={topMostBacklogCardId} /> }
    { !topMostBacklogCardId && <div className="empty">&nbsp;</div> }
  </div>
};

BacklogDeck.propTypes = {
  topMostBacklogCardId: PropTypes.string
};

const mapStateToProps = (state) => {
  const topMostCardId = state.backlogDeck ? state.backlogDeck[0] : null;
  return {
    topMostBacklogCardId: topMostCardId
  }
};

export default connect(
  mapStateToProps
)(BacklogDeck);
